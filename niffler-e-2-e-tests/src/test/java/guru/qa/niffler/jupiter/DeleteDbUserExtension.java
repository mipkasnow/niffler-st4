package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Map;

public class DeleteDbUserExtension implements AfterTestExecutionCallback {

    private final UserRepository userRepository = new UserRepositoryJdbc();

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        Map<String, Object> userEntities = (Map<String, Object>) extensionContext
                .getStore(DbUserExtension.NAMESPACE).get(extensionContext.getUniqueId());

        UserAuthEntity userAuth = (UserAuthEntity) userEntities.get("userAuth");
        UserEntity user = (UserEntity) userEntities.get("user");

        userRepository.deleteInAuthById(userAuth.getId());
        userRepository.deleteInUserdataById(user.getId());
    }
}
