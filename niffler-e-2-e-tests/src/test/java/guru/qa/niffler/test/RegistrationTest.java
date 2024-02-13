package guru.qa.niffler.test;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Веб тесты")
@Feature("Тесты регистрации")
public class RegistrationTest extends BaseWebTest{

    @Test
    @DisplayName("Пользователь может зарегистрироваться в системе")
    void userShouldSignUpTest() {
        String username = "Петя";
        String password = "12345";
        welcomePage.open().clickSignUpAndGoToRegisterForm().registerUserAndGoSignInPage(username, password)
                .signInUser(username, password).statisticsSectionShouldExist();
    }
}
