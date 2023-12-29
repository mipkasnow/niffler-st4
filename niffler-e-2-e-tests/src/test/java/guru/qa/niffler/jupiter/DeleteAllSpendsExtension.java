package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;

public class DeleteAllSpendsExtension implements BeforeEachCallback {

    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        CategoryJson categoryFromExtension = (CategoryJson) extensionContext.getStore(CategoryExtension.NAMESPACE).get("category");
        String username = categoryFromExtension.username();

        List<SpendJson> spends = spendApi.getSpends(username).execute().body();

        List<String> ids = spends.stream()
                .map(s -> s.id().toString())
                .toList();

        spendApi.deleteSpends(username, ids).execute();
    }

}
