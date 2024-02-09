package guru.qa.niffler.test;

import guru.qa.niffler.db.repository.SpendRepository;
import guru.qa.niffler.jupiter.SpendRepositoryExtension;
import guru.qa.niffler.jupiter.annotation.*;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Epic("Веб тесты")
@Feature("Spendings")
@ExtendWith(SpendRepositoryExtension.class)
public class SpendingTest extends BaseWebTest{

    private static SpendRepository spendRepository;

    @AfterAll
    static void deleteAllSpendsFromDb() {
        spendRepository.deleteAllSpendsInDb();
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
    @DisabledByIssue("74")
    @DisplayName("Пользователь может удалить Spending")
    void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
        welcomePage.open().clickLoginAndGoToSignInPage()
                .signInUser("duck", "12345")
                .deleteSpendingByButtonDelete(spend.description())
                .verifyEmptyListOfSpendings();
    }

    @GenerateSpendWithCategory(
            username = "bee",
            category = "Путешествия",
            description = "Тайланд",
            amount = 200000.00,
            currency = CurrencyValues.RUB
    )
    @Test
    @DisplayName("Пользователь может удалить Spending")
    void spendingShouldBeDeletedByButtonDeleteSpendingRest(SpendJson spend) {
        welcomePage.open().clickLoginAndGoToSignInPage()
                .signInUser("bee", "12345")
                .deleteSpendingByButtonDelete(spend.description())
                .verifyEmptyListOfSpendings();
    }
}
