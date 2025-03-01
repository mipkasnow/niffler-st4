package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.Database;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.sjdbc.UserAuthEntityResultSetExtractor;
import guru.qa.niffler.db.sjdbc.UserEntityRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositorySJdbc implements UserRepository {

  private final TransactionTemplate authTxt;
  private final TransactionTemplate udTxt;
  private final JdbcTemplate authTemplate;
  private final JdbcTemplate udTemplate;

  private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public UserRepositorySJdbc() {
    JdbcTransactionManager authTm = new JdbcTransactionManager(
        DataSourceProvider.INSTANCE.dataSource(Database.AUTH)
    );
    JdbcTransactionManager udTm = new JdbcTransactionManager(
        DataSourceProvider.INSTANCE.dataSource(Database.USERDATA)
    );

    this.authTxt = new TransactionTemplate(authTm);
    this.udTxt = new TransactionTemplate(udTm);
    this.authTemplate = new JdbcTemplate(authTm.getDataSource());
    this.udTemplate = new JdbcTemplate(udTm.getDataSource());
  }

  @Override
  public UserAuthEntity createInAuth(UserAuthEntity user) {
    KeyHolder kh = new GeneratedKeyHolder();
    return authTxt.execute(status -> {
      authTemplate.update(con -> {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO \"user\" " +
                "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                "VALUES (?, ?, ?, ?, ?, ?)",
            PreparedStatement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, user.getUsername());
        ps.setString(2, pe.encode(user.getPassword()));
        ps.setBoolean(3, user.getEnabled());
        ps.setBoolean(4, user.getAccountNonExpired());
        ps.setBoolean(5, user.getAccountNonLocked());
        ps.setBoolean(6, user.getCredentialsNonExpired());
        return ps;
      }, kh);

      user.setId((UUID) kh.getKeys().get("id"));

      authTemplate.batchUpdate("INSERT INTO \"authority\" " +
          "(user_id, authority) " +
          "VALUES (?, ?)", new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
          ps.setObject(1, user.getId());
          ps.setString(2, user.getAuthorities().get(i).getAuthority().name());
        }

        @Override
        public int getBatchSize() {
          return user.getAuthorities().size();
        }
      });

      return user;
    });
  }

  @Override
  public Optional<UserAuthEntity> findByIdInAuth(UUID id) {
    try {
      return Optional.ofNullable(
          authTemplate.query(
              "SELECT * " +
                  "FROM \"user\" u " +
                  "JOIN \"authority\" a ON u.id = a.user_id " +
                  "where u.id = ?",
              UserAuthEntityResultSetExtractor.instance,
              id
          )
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public UserEntity createInUserdata(UserEntity user) {
    KeyHolder kh = new GeneratedKeyHolder();
    udTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO \"user\" (username, currency) VALUES (?, ?)",
          PreparedStatement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      return ps;
    }, kh);

    user.setId((UUID) kh.getKeys().get("id"));
    return user;
  }

  @Override
  public Optional<UserEntity> findByIdInUserdata(UUID id) {
    try {
      return Optional.ofNullable(
          udTemplate.queryForObject(
              "SELECT * FROM \"user\" WHERE id = ?",
              UserEntityRowMapper.instance,
              id
          )
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public void deleteInAuthById(UUID id) {
    authTxt.execute(status -> {
      authTemplate.update("DELETE FROM \"authority\" WHERE user_id = ?", id);
      authTemplate.update("DELETE FROM \"user\" WHERE id = ?", id);
      return null;
    });
  }

  @Override
  public void deleteInUserdataById(UUID id) {
    udTxt.execute(status -> {
      udTemplate.update("DELETE FROM friendship WHERE friend_id = ?", id);
      udTemplate.update("DELETE FROM friendship WHERE user_id = ?", id);
      udTemplate.update("DELETE FROM \"user\" WHERE id = ?", id);
      return null;
    });
  }

  @Override
  public void updateCurrencyByUsername(String name, CurrencyValues currency) {

  }

  @Override
  public List<UserEntity> getAllUsersData() {
    return null;
  }

  @Override
  public UserEntity getUserDataByName(String name) {
    return null;
  }

  @Override
  public void blockUserByNameInAuth(String name) {

  }

  @Override
  public UserAuthEntity updateInAuth(UserAuthEntity user) {
    return authTxt.execute(status -> {
      authTemplate.update(con -> {
        PreparedStatement userPs = con.prepareStatement("UPDATE \"user\" " +
                "SET password = ?, enabled = ?, account_non_expired = ?, " +
                "account_non_locked = ?, credentials_non_expired = ? WHERE id = ?");
        userPs.setObject(1, pe.encode(user.getPassword()));
        userPs.setBoolean(2, user.getEnabled());
        userPs.setBoolean(3, user.getAccountNonExpired());
        userPs.setBoolean(4, user.getAccountNonLocked());
        userPs.setBoolean(5, user.getCredentialsNonExpired());
        userPs.setObject(6, user.getId());
        return userPs;
      });
      authTemplate.update("DELETE FROM \"authority\" WHERE user_id = ?", user.getId());
      authTemplate.batchUpdate("INSERT INTO \"authority\" " +
              "(user_id, authority) " +
              "VALUES (?, ?)", new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
          ps.setObject(1, user.getId());
          ps.setString(2, user.getAuthorities().get(i).getAuthority().name());
        }

        @Override
        public int getBatchSize() {
          return user.getAuthorities().size();
        }
      });
      return user;
    });
  }

  @Override
  public UserEntity updateInUserdata(UserEntity user) {
    udTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
              "UPDATE \"user\" SET currency=?, firstname=?, surname=? WHERE id=?"
      );
      ps.setString(1, user.getCurrency().name());
      ps.setString(2, user.getFirstname());
      ps.setString(3, user.getSurname());
      ps.setObject(4, user.getId());
      return ps;
    });
    return user;
  }


}
