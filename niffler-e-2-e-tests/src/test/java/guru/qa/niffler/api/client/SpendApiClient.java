package guru.qa.niffler.api.client;

import guru.qa.niffler.api.service.SpendApi;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;

public class SpendApiClient extends RestClient{

    private final SpendApi spendApi;

    public SpendApiClient() {
        super("http://127.0.0.1:8093");
        this.spendApi = retrofit.create(SpendApi.class);
    }

    @Step("Добавить spend POST addSpend")
    public SpendJson addSpend(SpendJson spend) throws IOException {
        return spendApi.addSpend(spend).execute().body();
    }

    @Step("Удалить все spends DELETE deleteSpends у юзера {username}")
    public void deleteSpends(String username) throws IOException {
        List<SpendJson> spends = spendApi.getSpends(username).execute().body();

        List<String> ids = spends.stream()
                .map(s -> s.id().toString())
                .toList();

        spendApi.deleteSpends(username, ids).execute();
    }

    //удалитть определенный спенд
    //получить все спенды

    @Step("Получение статистики GET getStatistic юзера {username}")
    public List<StatisticJson> getStatistic(String username, CurrencyValues userCurrency) throws IOException {
        return spendApi.getStatistic(username, userCurrency).execute().body();
    }

    @Step("Редактировать spend PATCH editSpend")
    public SpendJson editSpend(SpendJson spend) throws IOException {
        return spendApi.editSpend(spend).execute().body();
    }
}
