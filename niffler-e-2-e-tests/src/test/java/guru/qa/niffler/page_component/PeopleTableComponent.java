package guru.qa.niffler.page_component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PeopleTableComponent {

    private final SelenideElement
            peopleTable = $(".people-content tbody");

    private final String
            submitInvitationButtonSelector = "[data-tooltip-id='submit-invitation']",
            removeFriendButtonSelector = "[data-tooltip-id='remove-friend']";

    public PeopleTableComponent verifyFriendsListNotEmpty() {
        peopleTable.$$("tr")
                .find(text("You are friends"))
                .shouldBe(visible);

        return this;
    }

    public PeopleTableComponent verifyReceivedFriendsInvitation() {
        peopleTable.$$("tr")
                .first()
                .$(submitInvitationButtonSelector)
                .shouldBe(visible);

        return this;
    }

    public PeopleTableComponent verifyPendingFriendsInvitation() {
        peopleTable.$$("tr")
                .find(text("Pending invitation"))
                .shouldBe(visible);

        return this;
    }

    public PeopleTableComponent verifyExistingButtonRemoveFriend() {
        peopleTable.$$("tr")
                .first()
                .$(removeFriendButtonSelector)
                .shouldBe(visible);

        return this;
    }

    public PeopleTableComponent verifyPendingInvitationToUser(String username) {
        peopleTable.$$("tr")
                .first()
                .$$("td")
                .get(1)
                .shouldHave(text(username));

        return this;
    }


}
