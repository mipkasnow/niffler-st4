package guru.qa.niffler.page;

import guru.qa.niffler.page_component.HeaderComponent;
import guru.qa.niffler.page_component.PeopleTableComponent;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class PeoplePage {

    private final PeopleTableComponent peopleTableComponent = new PeopleTableComponent();
    private final HeaderComponent headerComponent = new HeaderComponent();

    @Step("Ожидание загрузки страницы people")
    public PeoplePage waitUntilLoaded() {
        webdriver().shouldHave(urlContaining("people"));
        return this;
    }

    @Step("Проверка наличия отправленного запроса дружить")
    public PeoplePage verifyPendingFriendsInvitation() {
        peopleTableComponent.verifyPendingFriendsInvitation();
        return this;
    }

    @Step("Перейти на страницу профиля")
    public ProfilePage goToProfile() {
        headerComponent.clickProfilePage();
        return new ProfilePage().waitUntilLoaded();
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

    @Step("Проверить отправленное приглашение пользователю {userName}")
    public PeoplePage verifyPendingInvitationToUser(String username) {
        peopleTableComponent.verifyPendingInvitationToUser(username);
        return this;
    }
}
