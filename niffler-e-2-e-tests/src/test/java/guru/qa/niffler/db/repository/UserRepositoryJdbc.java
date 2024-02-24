package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.Database;
import guru.qa.niffler.db.model.*;
import io.qameta.allure.Step;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryJdbc implements UserRepository {

  private final DataSource authDs = DataSourceProvider.INSTANCE.dataSource(Database.AUTH);
  private final DataSource udDs = DataSourceProvider.INSTANCE.dataSource(Database.USERDATA);

  private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Step("Create user in AUTH")
  @Override
  public UserAuthEntity createInAuth(UserAuthEntity user) {
    try (Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);

      try (PreparedStatement userPs = conn.prepareStatement(
          "INSERT INTO \"user\" " +
              "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
              "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
           PreparedStatement authorityPs = conn.prepareStatement(
               "INSERT INTO \"authority\" " +
                   "(user_id, authority) " +
                   "VALUES (?, ?)")
      ) {

        userPs.setString(1, user.getUsername());
        userPs.setString(2, pe.encode(user.getPassword()));
        userPs.setBoolean(3, user.getEnabled());
        userPs.setBoolean(4, user.getAccountNonExpired());
        userPs.setBoolean(5, user.getAccountNonLocked());
        userPs.setBoolean(6, user.getCredentialsNonExpired());

        userPs.executeUpdate();

        UUID authUserId;
        try (ResultSet keys = userPs.getGeneratedKeys()) {
          if (keys.next()) {
            authUserId = UUID.fromString(keys.getString("id"));
          } else {
            throw new IllegalStateException("Can`t find id");
          }
        }

        for (Authority authority : Authority.values()) {
          authorityPs.setObject(1, authUserId);
          authorityPs.setString(2, authority.name());
          authorityPs.addBatch();
          authorityPs.clearParameters();
        }

        authorityPs.executeBatch();
        conn.commit();
        user.setId(authUserId);
      } catch (Exception e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public Optional<UserAuthEntity> findByIdInAuth(UUID id) {
    try (Connection conn = authDs.getConnection();
         PreparedStatement usersPs = conn.prepareStatement("SELECT * " +
             "FROM \"user\" u " +
             "JOIN \"authority\" a ON u.id = a.user_id " +
             "where u.id = ?")) {
      usersPs.setObject(1, id);

      usersPs.execute();
      UserAuthEntity user = new UserAuthEntity();
      boolean userProcessed = false;
      try (ResultSet resultSet = usersPs.getResultSet()) {
        while (resultSet.next()) {
          if (!userProcessed) {
            user.setId(resultSet.getObject(1, UUID.class));
            user.setUsername(resultSet.getString(2));
            user.setPassword(resultSet.getString(3));
            user.setEnabled(resultSet.getBoolean(4));
            user.setAccountNonExpired(resultSet.getBoolean(5));
            user.setAccountNonLocked(resultSet.getBoolean(6));
            user.setCredentialsNonExpired(resultSet.getBoolean(7));
            userProcessed = true;
          }

          AuthorityEntity authority = new AuthorityEntity();
          authority.setId(resultSet.getObject(8, UUID.class));
          authority.setAuthority(Authority.valueOf(resultSet.getString(10)));
          user.getAuthorities().add(authority);
        }
      }
      return userProcessed ? Optional.of(user) : Optional.empty();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UserEntity createInUserdata(UserEntity user) {
    try (Connection conn = udDs.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO \"user\" " +
              "(username, currency) " +
              "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getCurrency().name());
        ps.executeUpdate();

        UUID userId;
        try (ResultSet keys = ps.getGeneratedKeys()) {
          if (keys.next()) {
            userId = UUID.fromString(keys.getString("id"));
          } else {
            throw new IllegalStateException("Can`t find id");
          }
        }
        user.setId(userId);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public Optional<UserEntity> findByIdInUserdata(UUID id) {
    UserEntity user = new UserEntity();
    try (Connection conn = udDs.getConnection();
         PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM \"user\" WHERE id = ? ")) {
      usersPs.setObject(1, id);
      usersPs.execute();
      try (ResultSet resultSet = usersPs.getResultSet()) {
        if (resultSet.next()) {
          user.setId(resultSet.getObject("id", UUID.class));
          user.setUsername(resultSet.getString("username"));
          user.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
          user.setFirstname(resultSet.getString("firstname"));
          user.setSurname(resultSet.getString("surname"));
          user.setPhoto(resultSet.getBytes("photo"));
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return Optional.of(user);
  }

  @Override
  public void deleteInAuthById(UUID id) {
    try (Connection conn = authDs.getConnection()){
      conn.setAutoCommit(false);

      try (PreparedStatement authorityPs = conn.prepareStatement("DELETE FROM \"authority\"  WHERE user_id='" + id + "'");
           PreparedStatement userPs = conn.prepareStatement("DELETE FROM \"user\" WHERE id='" + id + "'")) {
        authorityPs.execute();
        userPs.execute();
        conn.commit();
      } catch (Exception e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteInUserdataById(UUID id) {
    try (Connection conn = udDs.getConnection()){

      try (PreparedStatement ps = conn.prepareStatement("DELETE FROM \"user\" WHERE id='" + id + "'")){
        ps.execute();
      }

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
  }

  @Override
  public void updateCurrencyByUsername(String userName, CurrencyValues currency) {
    try (Connection conn = udDs.getConnection()){

      try (PreparedStatement ps = conn.prepareStatement("UPDATE \"user\" SET currency='" + currency + "' where username ='" + userName + "'")){
        ps.execute();
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<UserEntity> getAllUsersData() {
    List<UserEntity> users = new ArrayList<>();

    try (Connection conn = udDs.getConnection()){

      try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM \"user\"")){
        try (ResultSet resultSet = ps.executeQuery()){
          while (resultSet.next()) {
            UserEntity user = new UserEntity();
            user.setId(UUID.fromString(resultSet.getString("id")));
            user.setUsername(resultSet.getString("username"));
            user.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
            user.setFirstname(resultSet.getString("firstname"));
            user.setSurname(resultSet.getString("surname"));
            user.setPhoto(resultSet.getBytes("photo"));
            users.add(user);
          }
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return users;
  }

  @Override
  public UserEntity getUserDataByName(String name) {
    UserEntity user = new UserEntity();

    try (Connection conn = udDs.getConnection()){

      try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM \"user\" WHERE username='" + name + "'")){
        try (ResultSet resultSet = ps.executeQuery()){
          while (resultSet.next()) {
            user.setId(UUID.fromString(resultSet.getString("id")));
            user.setUsername(resultSet.getString("username"));
            user.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
            user.setFirstname(resultSet.getString("firstname"));
            user.setSurname(resultSet.getString("surname"));
            user.setPhoto(resultSet.getBytes("photo"));
          }
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return user;
  }

  @Override
  public void blockUserByNameInAuth(String name) {
    try (Connection conn = authDs.getConnection()){

      try (PreparedStatement ps = conn.prepareStatement("UPDATE \"user\" SET enabled=false, account_non_expired=false, " +
              "account_non_locked=false, credentials_non_expired=false WHERE username='" + name + "'")){
        ps.execute();
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UserEntity updateInUserdata(UserEntity user) {
    try (Connection conn = udDs.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(
              "UPDATE \"user\" SET currency=?, firstname=?, surname=? WHERE id=?", PreparedStatement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, user.getCurrency().name());
        ps.setString(2, user.getFirstname());
        ps.setString(3, user.getSurname());
        ps.setObject(4, user.getId());
        ps.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public UserAuthEntity updateInAuth(UserAuthEntity user) {
    try (Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement userPs = conn.prepareStatement("UPDATE \"user\" " +
              "SET password = ?, enabled = ?, account_non_expired = ?, " +
              "account_non_locked = ?, credentials_non_expired = ? WHERE id = ?");
           PreparedStatement authorityInsertPs = conn.prepareStatement(
                   "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)");
           PreparedStatement authorityDeletePs = conn.prepareStatement(
                   "DELETE FROM \"authority\" WHERE user_id = ?");
      ) {
        userPs.setObject(1, pe.encode(user.getPassword()));
        userPs.setBoolean(2, user.getEnabled());
        userPs.setBoolean(3, user.getAccountNonExpired());
        userPs.setBoolean(4, user.getAccountNonLocked());
        userPs.setBoolean(5, user.getCredentialsNonExpired());
        userPs.setObject(6, user.getId());
        userPs.executeUpdate();

        authorityDeletePs.setObject(1, user.getId());
        authorityDeletePs.executeUpdate();

        for (Authority authority : Authority.values()) {
          authorityInsertPs.setObject(1, user.getId());
          authorityInsertPs.setString(2, authority.name());
          authorityInsertPs.addBatch();
          authorityInsertPs.clearParameters();
        }
        authorityInsertPs.executeBatch();

        conn.commit();
        return user;
      } catch (SQLException e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
