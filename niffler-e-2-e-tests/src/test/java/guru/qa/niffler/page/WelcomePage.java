package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class WelcomePage {

    private final SelenideElement
            loginButton = $("a[href*='redirect']"),
            registerButton = $("a[href*='register']"),
            title = $(withText("Welcome to magic journey with Niffler. The coin keeper")),
            logo = $("[alt='Logo Niffler']");

    @Step("Ожидание загрузки начальной страницы приложения")
    public WelcomePage waitUntilLoaded() {
        title.should(appear);
        logo.should(appear);

        return this;
    }

    @Step("Нажать кнопку логина и перейти на страницу авторизации")
    public SignInPage clickLoginAndGoToSignInPage() {
        loginButton.click();

        return new SignInPage().waitUntilLoaded();
    }

    public static class SignInPage {

        private final SelenideElement
                subTitle = $(withText("Please sign in")),
                userNameInput = $("input[name='username']"),
                passwordInput = $("input[name='password']"),
                signInButton = $("button[type='submit']");

        @Step("Ожидание загрузки страницы входа в приложение")
        public SignInPage waitUntilLoaded() {
            subTitle.should(appear);

            return this;
        }

        @Step("Залогиниться на главную страницу пользователем {userName}")
        public MainPage signInUser(String userName, String password) {
            userNameInput.setValue(userName);
            passwordInput.setValue(password);
            signInButton.click();

            return new MainPage().waitUntilLoaded();
        }
    }
}
