package guru.qa.niffler.page_component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.SpendJson;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.SpendCollectionCondition.spends;

public class HistoryOfSpendingsComponent extends BaseComponent<HistoryOfSpendingsComponent>{

    public HistoryOfSpendingsComponent() {
        super($("section[class*='history']"));
    }

    private final SelenideElement
            title = $(withText("History of spendings")),
            subtitle = $(withText("All the limits are in your head")),
            tableOfSpendings = $(".spendings-table tbody"),
            buttonDeleteSelectedSpending = $(byText("Delete selected"));

    public HistoryOfSpendingsComponent waitUntilLoaded() {
        title.should(appear);
        subtitle.should(appear);

        return this;
    }

    public HistoryOfSpendingsComponent deleteSpendingByButtonDeleteSpending(String spendDescription) {
        tableOfSpendings
                .$$("tr")
                .find(text(spendDescription))
                .$("td [type='checkbox']").scrollTo()
                .click();
        buttonDeleteSelectedSpending.shouldNotHave(attribute("disabled"))
                .click();

        return this;
    }

    public HistoryOfSpendingsComponent verifyEmptyListOfSpendings() {
        tableOfSpendings.$$("tr")
                .shouldHave(size(0));

        return this;
    }

    public HistoryOfSpendingsComponent checkSpends(SpendJson... expectedSpends) {
        tableOfSpendings.$$("tr").shouldHave(spends(expectedSpends));
        return this;
    }

    public HistoryOfSpendingsComponent selectSpendingByIndex(int index) {
        tableOfSpendings.$$("tr").get(index).$("[type='checkbox']").scrollTo().click();
        buttonDeleteSelectedSpending.shouldNotHave(attribute("disabled"));
        return this;
    }

    public HistoryOfSpendingsComponent unSelectSpendingByIndex(int index) {
        tableOfSpendings.$$("tr").get(index).$("[type='checkbox']").scrollTo().click();
        return this;
    }

    public HistoryOfSpendingsComponent selectSpendingByDescription(String description) {
        tableOfSpendings.$$("tr").find(text(description)).$("[type='checkbox']").scrollTo().click();
        buttonDeleteSelectedSpending.shouldNotHave(attribute("disabled"));
        return this;
    }

    public HistoryOfSpendingsComponent unSelectSpendingByDescription(String description) {
        tableOfSpendings.$$("tr").find(text(description)).$("[type='checkbox']").scrollTo().click();
        return this;
    }

}
