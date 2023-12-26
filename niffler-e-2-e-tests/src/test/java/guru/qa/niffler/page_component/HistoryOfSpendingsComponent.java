package guru.qa.niffler.page_component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class HistoryOfSpendingsComponent {

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

    public HistoryOfSpendingsComponent deleteSingleSpendingByButtonDeleteSpending(String spendDescription) {
        tableOfSpendings
                .$$("tr")
                .find(text(spendDescription))
                .$("td [type='checkbox']").scrollTo()
                .click();
        buttonDeleteSelectedSpending.shouldNotHave(attribute("disabled"))
                .click();

        tableOfSpendings.$$("tr")
                .shouldHave(size(0));

        return this;
    }


}
