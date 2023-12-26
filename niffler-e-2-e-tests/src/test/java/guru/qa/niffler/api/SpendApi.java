package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {

    @POST("/addSpend")
    Call<SpendJson> addSpend(@Body SpendJson spend);

    @POST("/category")
    Call<CategoryJson> addCategory(@Body CategoryJson category);

    @DELETE("/deleteSpends")
    Call<Void> deleteSpends(@Query("username") String username, @Query("ids") List<String> ids);

    @GET("/spends")
    Call<List<SpendJson>> getSpends(@Query("username") String username);
}
