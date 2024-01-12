package guru.qa.niffler.page;

import guru.qa.niffler.page_component.HeaderComponent;
import guru.qa.niffler.page_component.ProfileInfoComponent;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class ProfilePage {

    private final ProfileInfoComponent profileInfoComponent = new ProfileInfoComponent();
    private final HeaderComponent headerComponent = new HeaderComponent();

    @Step("Ожидание загрузки страницы профиля")
    public ProfilePage waitUntilLoaded() {
        webdriver().shouldHave(urlContaining("profile"));
        return this;
    }

    @Step("Проверка имени профиля {username}")
    public ProfilePage verifyProfileName(String username){
        profileInfoComponent.verifyProfileName(username);
        return this;
    }

    @Step("Перейти на страницу people")
    public PeoplePage goToPeoplePage() {
        headerComponent.clickPeoplePage();
        return new PeoplePage().waitUntilLoaded();
    }

    @Step("Перейти на страницу friends")
    public FriendsPage goToFriendsPage() {
        headerComponent.clickFriendsPage();
        return new FriendsPage().waitUntilLoaded();
    }

    @Step("Перейти на главную страницу ")
    public MainPage goToMainPage() {
        headerComponent.clickMainPage();
        return new MainPage().waitUntilLoaded();
    }
}
