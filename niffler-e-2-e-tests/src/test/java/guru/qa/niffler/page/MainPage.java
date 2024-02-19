package guru.qa.niffler.page;

import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.message.SuccessMsg;
import guru.qa.niffler.page_component.HeaderComponent;
import guru.qa.niffler.page_component.HistoryOfSpendingsComponent;
import guru.qa.niffler.page_component.StatisticsComponent;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;

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

    @Step("Проверить все spendings пользователя {username}")
    public MainPage checkAllSpends(String username) throws IOException {
        List<SpendJson> spends = spendApiClient.getSpends(username);
        historyOfSpendingsComponent.checkSpends(spends.toArray(new SpendJson[spends.size()]));
        return this;
    }

    @Step("Выделить spend по индексу {index}")
    public MainPage selectSpendingByIndex(int index) {
        historyOfSpendingsComponent.selectSpendingByIndex(index);
        return this;
    }

    @Step("Снять выделение spend по индексу {index}")
    public MainPage unSelectSpendingByIndex(int index) {
        historyOfSpendingsComponent.unSelectSpendingByIndex(index);
        return this;
    }

    @Step("Выделить spend по описанию {description}")
    public MainPage selectSpendingByDescription(String description) {
        historyOfSpendingsComponent.selectSpendingByDescription(description);
        return this;
    }

    @Step("Снять выделение spend по описанию {description}")
    public MainPage unSelectSpendingByDescription(String description) {
        historyOfSpendingsComponent.selectSpendingByDescription(description);
        return this;
    }
}
