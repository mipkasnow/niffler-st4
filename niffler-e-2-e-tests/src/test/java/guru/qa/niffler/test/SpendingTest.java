package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.DeleteSpendsIfUserHasSome;
import guru.qa.niffler.jupiter.DisabledByIssue;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Веб тесты")
@Feature("Spendings")
public class SpendingTest extends BaseWebTest{

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
    @DisabledByIssue("74")
    @DisplayName("Пользователь может удалить Spending")
    void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
        welcomePage.open().clickLoginAndGoToSignInPage()
                .signInUser("duck", "12345")
                .deleteSpendingByButtonDelete(spend.description())
                .verifyEmptyListOfSpendings();
    }
}
