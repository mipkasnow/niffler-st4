package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.client.CategoryApiClient;
import guru.qa.niffler.api.client.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;

public class RestSpendExtension extends SpendWithCategoryExtension {

    private final CategoryApiClient categoryApiClient = new CategoryApiClient();
    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    @SneakyThrows
    protected SpendJson create(SpendJson spend) {
        CategoryJson categoryJson = new CategoryJson(
                null,
                spend.category(),
                spend.username()
        );
        categoryApiClient.addCategory(categoryJson);
        return spendApiClient.addSpend(spend);
    }
}
