package guru.qa.niffler.test;

import guru.qa.niffler.db.repository.SpendRepository;
import guru.qa.niffler.jupiter.annotation.DeleteSpendsIfUserHasSome;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateSpendWithCategory;
import guru.qa.niffler.jupiter.extension.SpendRepositoryExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

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
    //@DisabledByIssue("74")
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
    void spendingShouldBeDeletedByButtonDeleteSpendingRest(SpendJson spend) throws IOException {
        welcomePage.open().clickLoginAndGoToSignInPage()
                .signInUser("bee", "12345")
                .deleteSpendingByButtonDelete(spend.description())
                .verifyEmptyListOfSpendings();
    }

    @Test
    @DisplayName("Проверяем список spendings в таблице")
    void checkSeveralSpendingsTest() throws IOException {
        String username = "duck";

        for (int i = 0; i < 2; i++) {
            spendApiClient.addRandomSpend(username);
        }

        SpendJson spendJson = spendApiClient.addRandomSpend(username);

        welcomePage.open().clickLoginAndGoToSignInPage()
                .signInUser(username, "12345")
                .checkAllSpends(username).selectSpendingByIndex(0).unSelectSpendingByIndex(0)
                .selectSpendingByDescription(spendJson.description());
    }
}
