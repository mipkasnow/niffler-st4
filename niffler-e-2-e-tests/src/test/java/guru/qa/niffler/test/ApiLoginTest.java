package guru.qa.niffler.test;

import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.ContextHolderExtension;
import guru.qa.niffler.jupiter.extension.DbUserExtension;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith({ContextHolderExtension.class, DbUserExtension.class, ApiLoginExtension.class})
public class ApiLoginTest extends BaseWebTest{

    @ApiLogin(user = @DbUser)
    @Test
    void successfulApiLogin(UserAuthEntity userAuth) {
        open(mainPage.URL, MainPage.class).waitUntilLoaded()
                .goToProfile().verifyProfileName(userAuth.getUsername());
    }

    @ApiLogin(username = "duck", password = "12345")
    @Test
    void successfulApiLogin2() {
        open(mainPage.URL, MainPage.class).waitUntilLoaded()
                .goToProfile().verifyProfileName("duck");
    }

    @Test
    @DbUser()
    void dbUserTest(UserAuthEntity userAuth) {
        welcomePage.open().clickLoginAndGoToSignInPage().signInUser(
                userAuth.getUsername(),
                userAuth.getPassword()
        ).goToProfile().verifyProfileName(userAuth.getUsername());
    }
}
