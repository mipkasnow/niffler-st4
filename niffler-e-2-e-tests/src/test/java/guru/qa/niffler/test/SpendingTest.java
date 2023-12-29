package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.DeleteSpendsIfUserHasSome;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.WelcomePage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Веб тесты")
@Feature("Spendings")
public class SpendingTest {

    private final WelcomePage welcomePage = new WelcomePage();

    static {
        Configuration.browserSize = "1980x1024";
        Configuration.browser = "firefox";
    }

    @GenerateCategory(
            category = "Обучение",
            username = "duck"
    )
    @DeleteSpendsIfUserHasSome
    @GenerateSpend(
            username = "duck",
            description = "QA.GURU Advanced 4",
            amount = 72500.00,
            currency = CurrencyValues.RUB
    )
    @Test
    @DisplayName("Пользователь может удалить Spending")
    void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
        welcomePage.open().clickLoginAndGoToSignInPage()
                .signInUser("duck", "12345")
                .deleteSpendingByButtonDelete(spend.description())
                .verifyEmptyListOfSpendings();
    }
}
