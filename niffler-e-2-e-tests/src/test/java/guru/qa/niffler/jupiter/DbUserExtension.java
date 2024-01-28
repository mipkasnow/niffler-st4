package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DbUserExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(DbUserExtension.class);

    private UserAuthEntity userAuth =  new UserAuthEntity();
    private UserEntity user = new UserEntity();
    private final Faker faker = new Faker();
    private final UserRepository userRepository = new UserRepositoryJdbc();

    private static final String USER_AUTH_KEY = "userAuth";
    private static final String USER_KEY = "user";

    private String userName;
    private String password;
    private boolean deleteAfterTest;

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<DbUser> dbUser = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                DbUser.class
        );

        if (dbUser.isPresent()) {
            DbUser dbUserData = dbUser.get();
            this.userName = "".equals(dbUserData.username()) ? faker.name().firstName() : dbUserData.username();
            this.password = "".equals(dbUserData.password())
                    ? String.valueOf(faker.number().numberBetween(10000, 99999))
                    : dbUserData.password();
            this.deleteAfterTest = dbUserData.deleteAfterTest();
        }

        userAuth.setUsername(this.userName);
        userAuth.setPassword(this.password);
        userAuth.setEnabled(true);
        userAuth.setAccountNonExpired(true);
        userAuth.setAccountNonLocked(true);
        userAuth.setCredentialsNonExpired(true);
        userAuth.setAuthorities(Arrays.stream(Authority.values())
                .map(e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(e);
                    return ae;
                }).toList()
        );

        user.setUsername(this.userName);
        user.setCurrency(CurrencyValues.RUB);

        userRepository.createInAuth(userAuth);
        userRepository.createInUserdata(user);

        Map<String, Object> userEntities = new HashMap<>();
        userEntities.put(USER_AUTH_KEY, userAuth);
        userEntities.put(USER_KEY, user);

        extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), userEntities);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        if(this.deleteAfterTest) {
            Map<String, Object> userEntities = (Map<String, Object>) extensionContext
                    .getStore(DbUserExtension.NAMESPACE).get(extensionContext.getUniqueId());

            UserAuthEntity userAuth = (UserAuthEntity) userEntities.get(USER_AUTH_KEY);
            UserEntity user = (UserEntity) userEntities.get(USER_KEY);

            userRepository.deleteInAuthById(userAuth.getId());
            userRepository.deleteInUserdataById(user.getId());
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(UserAuthEntity.class);
    }

    @Override
    public UserAuthEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (UserAuthEntity) extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class)
                .get(USER_AUTH_KEY);
    }
}
