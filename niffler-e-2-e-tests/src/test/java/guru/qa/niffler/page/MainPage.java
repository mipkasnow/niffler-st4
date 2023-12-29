package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page_component.HistoryOfSpendingsComponent;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final HistoryOfSpendingsComponent historyOfSpendingsComponent = new HistoryOfSpendingsComponent();

    private final SelenideElement
            toast = $("[class*='toast']");

    @Step("Ожидание загрузки главной страницы")
    public MainPage waitUntilLoaded() {
        historyOfSpendingsComponent.waitUntilLoaded();

        return this;
    }

    @Step("Удалить Spending по названию {spendDescription}")
    public MainPage deleteSpendingByButtonDelete(String spendDescription) {
        historyOfSpendingsComponent.deleteSpendingByButtonDeleteSpending(spendDescription);
        toast.shouldHave(text("Spendings deleted"));

        return this;
    }

    @Step("Проверить пустой список Spendings")
    public MainPage verifyEmptyListOfSpendings() {
        historyOfSpendingsComponent.verifyEmptyListOfSpendings();

        return this;
    }


}
