package guru.qa.niffler.api.client;

import guru.qa.niffler.api.service.CurrencyApi;
import guru.qa.niffler.model.CurrencyJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;

public class CurrencyApiClient extends RestClient{

    private final CurrencyApi currencyApi;

    public CurrencyApiClient() {
        super("http://127.0.0.1:8091");
        this.currencyApi = retrofit.create(CurrencyApi.class);
    }

    @Step("Получить список currency GET getAllCurrencies")
    public List<CurrencyJson> getAllCurrencies() throws IOException {
        return currencyApi.getAllCurrencies().execute().body();
    }

}
