package guru.qa.niffler.test;

import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.jupiter.DbUser;
import guru.qa.niffler.jupiter.DeleteDbUser;
import guru.qa.niffler.jupiter.UserRepositoryExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UserRepositoryExtension.class)
public class LoginTest extends BaseWebTest {

  private UserRepository userRepository;

  @Test
  @DbUser
  @DeleteDbUser
  void statisticShouldBeVisibleAfterLogin(UserAuthEntity userAuth) {
    welcomePage.open().clickLoginAndGoToSignInPage().signInUser(
            userAuth.getUsername(),
            userAuth.getPassword()
    ).statisticsSectionShouldExist();
  }

  @Test
  @DbUser
  @DeleteDbUser
  void shouldChangeProfileName(UserAuthEntity userAuth) {
    welcomePage.open().clickLoginAndGoToSignInPage().signInUser(
            userAuth.getUsername(),
            userAuth.getPassword()
    ).goToProfile().verifyDefaultCurrencyValue(CurrencyValues.RUB);

    userRepository.updateCurrencyByUsername(userAuth.getUsername(), CurrencyValues.EUR);
    profilePage.refreshProfilePage().verifyDefaultCurrencyValue(CurrencyValues.EUR);
  }

  @Test
  @DbUser
  @DeleteDbUser
  public void  shouldSetFirstAndSirName(UserAuthEntity userAuth) {
    welcomePage.open().clickLoginAndGoToSignInPage().signInUser(
            userAuth.getUsername(),
            userAuth.getPassword()
    ).goToProfile();

    String firstName = faker.name().firstName();
    String surName = faker.name().lastName();

    profilePage.setFirstAndSurName(firstName, surName);

    UserEntity user = userRepository.getUserDataByName(userAuth.getUsername());
    Assertions.assertEquals(firstName, user.getFirstname());
    Assertions.assertEquals(surName, user.getSurname());
  }
}
