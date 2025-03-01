package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class WelcomePage extends BasePage<WelcomePage>{

    private final SelenideElement
            loginButton = $("a[href*='redirect']"),
            registerButton = $("a[href*='register']"),
            title = $(withText("Welcome to magic journey with Niffler. The coin keeper")),
            logo = $("[alt='Logo Niffler']");
    private static final String MAIN_URL = Config.getInstance().frontUrl();

    @Override
    @Step("Ожидание загрузки начальной страницы приложения")
    public WelcomePage waitUntilLoaded() {
        title.should(appear);
        logo.should(appear);

        return this;
    }

    @Step("Открыть начальную страницу приложения")
    public WelcomePage open() {
        Selenide.open(MAIN_URL);
        return this.waitUntilLoaded();
    }

    @Step("Нажать кнопку логина и перейти на страницу авторизации")
    public SignInPage clickLoginAndGoToSignInPage() {
        loginButton.click();
        return new SignInPage().waitUntilLoaded();
    }

    @Step("Пройти на форму регистрации")
    public SignUpPage clickSignUpAndGoToRegisterForm() {
        registerButton.click();
        return new SignUpPage().waitUntilLoaded();
    }

    public static class SignInPage extends BasePage<SignInPage>{

        private final SelenideElement errorField = $(".form__error");
        private final String blockedUserMessage = "Учетная запись пользователя заблокирована";

        private final SelenideElement
                subTitle = $(withText("Please sign in")),
                userNameInput = $("input[name='username']"),
                passwordInput = $("input[name='password']"),
                signInButton = $("button[type='submit']");

        @Override
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

        @Step("Ввести имя пользователя {userName}")
        public SignInPage setUserName(String userName) {
            userNameInput.setValue(userName);
            return this;
        }

        @Step("Ввести пароль")
        public SignInPage setPassword(String password) {
            passwordInput.setValue(password);
            return this;
        }

        @Step("Нажать кнопку логина")
        public SignInPage clickLoginButton() {
            signInButton.click();
            return this;
        }

        @Step("Проверка появления сообщения" + blockedUserMessage)
        public SignInPage blockedUserMessageShouldAppear() {
            errorField.shouldHave(text(blockedUserMessage));
            return this;
        }
    }

    public static class SignUpPage extends BasePage<SignUpPage> {

        private final SelenideElement
                subTitle = $(withText("Registration form")),
                userNameInput = $("input[name='username']"),
                passwordInput = $("input[name='password']"),
                passwordInputSubmit = $("input[name='passwordSubmit']"),
                signUpButton = $("button[type='submit']"),
                signInRedirectBtn = $(withText("Sign in!")),
                successRegistrationMsg = $(withText("Congratulations! You've registered!"));

        @Override
        @Step("Ожидание загрузки страницы регистрации")
        public SignUpPage waitUntilLoaded() {
            subTitle.should(appear);
            return this;
        }

        @Step
        public SignInPage registerUserAndGoSignInPage(String userName, String password) {
            userNameInput.setValue(userName);
            passwordInput.setValue(password);
            passwordInputSubmit.setValue(password);
            signUpButton.click();
            successRegistrationMsg.should(appear);
            signInRedirectBtn.click();
            return new SignInPage().waitUntilLoaded();
        }
    }
}
