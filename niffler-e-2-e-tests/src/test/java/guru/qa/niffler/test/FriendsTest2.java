package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SEND;

@ExtendWith(UsersQueueExtension.class)
public class FriendsTest2 extends BaseWebTest{

    @BeforeEach
    void doLogin() {
        welcomePage.open().clickLoginAndGoToSignInPage();
    }

    @Test
    @DisplayName("Проверка получения приглашения дружбы")
    void userShouldSeeFriendsInvitation(@User(INVITATION_RECEIVED) UserJson user) {
       signInPage.signInUser(user.username(), user.testData().password())
               .goToFriendsPage().verifyReceivedFriendsInvitation();
    }

    @Test
    @DisplayName("Пользователь видит свой отправленный запрос дружить")
    void friendsTableShouldNotBeEmpty2(@User(INVITATION_SEND) UserJson user) {
        signInPage.signInUser(user.username(), user.testData().password())
                .goToPeoplePage().verifyPendingFriendsInvitation();
    }

    @Test
    @DisplayName("Проверка отправленного запроса дружить конкретному юзеру")
    void userShouldSeeFriendsInvitation(@User(INVITATION_SEND) UserJson user,
                                        @User(INVITATION_RECEIVED) UserJson user2) {
        signInPage.signInUser(user.username(), user.testData().password())
                .goToPeoplePage().verifyPendingInvitationToUser(user2.username());
    }


}
