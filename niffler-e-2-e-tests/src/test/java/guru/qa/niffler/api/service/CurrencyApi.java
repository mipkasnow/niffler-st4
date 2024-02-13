package guru.qa.niffler.api.service;

import guru.qa.niffler.model.CurrencyJson;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface CurrencyApi {

    @GET("/getAllCurrencies")
    Call<List<CurrencyJson>> getAllCurrencies();
}
