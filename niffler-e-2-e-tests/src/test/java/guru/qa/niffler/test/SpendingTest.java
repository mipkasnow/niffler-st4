package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.DeleteAllSpends;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.WelcomePage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@Epic("Веб тесты")
@Feature("Spendings")
public class SpendingTest {

    private final WelcomePage welcomePage = new WelcomePage();
    private static final String MAIN_URL = "http://127.0.0.1:3000/main";

    static {
        Configuration.browserSize = "1980x1024";
        Configuration.browser = "firefox";
    }

    @GenerateCategory(
            category = "Обучение",
            username = "duck"
    )
    @DeleteAllSpends
    @GenerateSpend(
            username = "duck",
            description = "QA.GURU Advanced 4",
            amount = 72500.00,
            currency = CurrencyValues.RUB
    )
    @Test
    @DisplayName("Пользователь может удалить Spending")
    void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
        open(MAIN_URL);

        welcomePage.waitUntilLoaded().clickLoginAndGoToSignInPage()
                .signInUser("duck", "12345")
                .deleteSingleSpendingByButtonDeleteAndVerifyEmptyListOfSpendings(spend.description());
    }
}
