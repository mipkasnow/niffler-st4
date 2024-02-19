package guru.qa.niffler.page_component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class HeaderComponent extends BaseComponent<HeaderComponent>{

    public HeaderComponent() {
        super($(".header"));
    }

    private final SelenideElement
            profile = $("[href='/profile']"),
            people = $("[href='/people']"),
            friends = $("[href='/friends']"),
            main = $("[href='/main']"),
            invitationNotification = $(".header__sign");

    public HeaderComponent waitUntilLoaded() {
        profile.should(appear);
        return this;
    }

    public HeaderComponent clickFriendsPage() {
        friends.click();
        return this;
    }

    public HeaderComponent invitationNotificationShouldBeVisible() {
        invitationNotification.shouldBe(visible);
        return this;
    }

    public HeaderComponent clickPeoplePage() {
        people.click();
        return this;
    }

    public HeaderComponent clickProfilePage() {
        profile.click();
        return this;
    }

    public HeaderComponent clickMainPage() {
        main.click();
        return this;
    }
}
