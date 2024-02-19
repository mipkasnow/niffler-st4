package guru.qa.niffler.api.client;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.service.CategoryApi;
import guru.qa.niffler.api.service.SpendApi;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class SpendApiClient extends RestClient{

    private final SpendApi spendApi;
    private final CategoryApi categoryApi;
    private final Faker faker;

    public SpendApiClient() {
        super("http://127.0.0.1:8093");
        this.spendApi = retrofit.create(SpendApi.class);
        this.categoryApi = retrofit.create(CategoryApi.class);
        this.faker = new Faker();
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

    @Step("Создать рандомный spend пользователю {username}")
    public SpendJson addRandomSpend(String username) throws IOException {
        String randomCategory = faker.job().position();

        CategoryJson categoryJson = new CategoryJson(
                null,
                randomCategory,
                username
        );

        categoryApi.addCategory(categoryJson).execute();

        SpendJson spendJson = new SpendJson(
                null,
                new Date(),
                randomCategory,
                CurrencyValues.RUB,
                faker.number().randomDouble(2, 100, 100000),
                faker.commerce().productName(),
                username
        );

        return spendApi.addSpend(spendJson).execute().body();
    }

    @Step("Получение статистики GET getStatistic юзера {username}")
    public List<StatisticJson> getStatistic(String username, CurrencyValues userCurrency) throws IOException {
        return spendApi.getStatistic(username, userCurrency).execute().body();
    }

    @Step("Редактировать spend PATCH editSpend")
    public SpendJson editSpend(SpendJson spend) throws IOException {
        return spendApi.editSpend(spend).execute().body();
    }

    @Step("Получить все spends GET getSpends юзера {username}")
    public List<SpendJson> getSpends(String username) throws IOException {
        return spendApi.getSpends(username).execute().body();
    }
}
