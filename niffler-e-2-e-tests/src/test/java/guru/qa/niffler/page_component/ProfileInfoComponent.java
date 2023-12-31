package guru.qa.niffler.page_component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ProfileInfoComponent {

    private final SelenideElement
            container = $(".profile-content .main-content__section-avatar"),
            profileName = container.$("figcaption");

    public ProfileInfoComponent verifyProfileName(String username) {
        profileName.shouldHave(text(username));
        return this;
    }


}
