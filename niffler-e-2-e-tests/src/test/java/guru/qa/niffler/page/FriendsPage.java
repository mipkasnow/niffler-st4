package guru.qa.niffler.page;

import guru.qa.niffler.page_component.HeaderComponent;
import guru.qa.niffler.page_component.PeopleTableComponent;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class FriendsPage {

    private final PeopleTableComponent peopleTableComponent = new PeopleTableComponent();
    private final HeaderComponent headerComponent = new HeaderComponent();

    @Step("Ожидание загрузки страницы friends")
    public FriendsPage waitUntilLoaded() {
        webdriver().shouldHave(urlContaining("friends"));
        return this;
    }

    @Step("Проверка непустого списка друзей")
    public FriendsPage verifyThatFriendsListNotEmpty() {
        peopleTableComponent.verifyFriendsListNotEmpty();
        return this;
    }

    @Step("Проверка наличия приглашения дружить")
    public FriendsPage verifyReceivedFriendsInvitation() {
        headerComponent.invitationNotificationShouldBeVisible();
        peopleTableComponent.verifyReceivedFriendsInvitation();

        return this;
    }

    @Step("Проверка наличия кнопки 'Remove Friend'")
    public FriendsPage verifyExistingButtonRemoveFriend() {
        peopleTableComponent.verifyExistingButtonRemoveFriend();
        return this;
    }

    @Step("Перейти на страницу профиля")
    public ProfilePage goToProfile() {
        headerComponent.clickProfilePage();
        return new ProfilePage().waitUntilLoaded();
    }

    @Step("Перейти на страницу people")
    public PeoplePage goToPeoplePage() {
        headerComponent.clickPeoplePage();
        return new PeoplePage().waitUntilLoaded();
    }

    @Step("Перейти на главную страницу ")
    public MainPage goToMainPage() {
        headerComponent.clickMainPage();
        return new MainPage().waitUntilLoaded();
    }
}
