package guru.qa.niffler.page_component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.db.model.CurrencyValues;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class ProfileInfoComponent extends BaseComponent<ProfileInfoComponent>{

    public ProfileInfoComponent() {
        super($(".profile-content .main-content__section-avatar"));
    }

    private final SelenideElement
            container = $(".profile-content .main-content__section-avatar"),
            defaultCurrencyValue = container.$(withText("Currency")).parent().$("[class*='singleValue']"),
            inputFirstName = $("[name=firstname]"),
            inputSurName = $("[name=surname]"),
            submitButton = $("[type=submit]"),
            profileName = container.$("figcaption");

    public ProfileInfoComponent verifyProfileName(String username) {
        profileName.shouldHave(text(username));
        return this;
    }

    public ProfileInfoComponent verifyDefaultCurrencyValue(CurrencyValues currencyValue) {
        defaultCurrencyValue.shouldHave(text(currencyValue.toString()));
        return this;
    }

    public ProfileInfoComponent setFirstAndSurName(String firstName, String surName) {
        inputFirstName.setValue(firstName);
        inputSurName.setValue(surName);
        submitButton.scrollTo().click();

        return this;
    }


}
