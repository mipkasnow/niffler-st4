package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.WITH_FRIENDS;

@ExtendWith(UsersQueueExtension.class)
public class FriendsTest3 extends BaseWebTest{

    @BeforeEach
    void doLogin(@User(WITH_FRIENDS) UserJson user) {
        welcomePage.open().clickLoginAndGoToSignInPage()
                .signInUser(user.username(), user.testData().password());
    }

    @Test
    @DisplayName("Проверка навигации по основным разделам приложения")
    void navigationTest() {
        mainPage.goToFriendsPage().goToPeoplePage().goToProfile()
                .goToPeoplePage().goToFriendsPage().goToMainPage();
    }

}
