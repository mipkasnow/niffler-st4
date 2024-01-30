package guru.qa.niffler.page_component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.$;

public class StatisticsComponent {
    private final SelenideElement statisticsSection = $(".main-content__section-stats");

    public StatisticsComponent statisticsSectionShouldExist() {
        statisticsSection.should(appear);
        return this;
    }
}