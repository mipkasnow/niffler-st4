package guru.qa.niffler.page;

import guru.qa.niffler.page.message.SuccessMsg;
import guru.qa.niffler.page_component.HeaderComponent;
import guru.qa.niffler.page_component.HistoryOfSpendingsComponent;
import guru.qa.niffler.page_component.StatisticsComponent;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class MainPage extends BasePage<MainPage>{

    private final HistoryOfSpendingsComponent historyOfSpendingsComponent = new HistoryOfSpendingsComponent();
    private final HeaderComponent headerComponent = new HeaderComponent();
    private final StatisticsComponent statisticsComponent = new StatisticsComponent();

    @Override
    @Step("Ожидание загрузки главной страницы")
    public MainPage waitUntilLoaded() {
        webdriver().shouldHave(urlContaining("main"));
        return this;
    }

    @Step("Удалить Spending по названию {spendDescription}")
    public MainPage deleteSpendingByButtonDelete(String spendDescription) {
        historyOfSpendingsComponent.deleteSpendingByButtonDeleteSpending(spendDescription);
        checkToastMessage(SuccessMsg.SPENDINGS_DELETED);

        return this;
    }

    @Step("Проверить пустой список Spendings")
    public MainPage verifyEmptyListOfSpendings() {
        historyOfSpendingsComponent.verifyEmptyListOfSpendings();
        return this;
    }

    @Step("Перейти на страницу friends")
    public FriendsPage goToFriendsPage() {
        headerComponent.clickFriendsPage();
        return new FriendsPage().waitUntilLoaded();
    }

    @Step("Перейти на страницу people")
    public PeoplePage goToPeoplePage() {
        headerComponent.clickPeoplePage();
        return new PeoplePage().waitUntilLoaded();
    }

    @Step("Перейти на страницу профиля")
    public ProfilePage goToProfile() {
        headerComponent.clickProfilePage();
        return new ProfilePage().waitUntilLoaded();
    }

    @Step("Проверка наличия раздела Статистика")
    public MainPage statisticsSectionShouldExist() {
        statisticsComponent.statisticsSectionShouldExist();
        return this;
    }
}
