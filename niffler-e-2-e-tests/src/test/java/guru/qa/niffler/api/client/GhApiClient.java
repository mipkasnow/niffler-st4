package guru.qa.niffler.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.service.GhApi;
import io.qameta.allure.Step;

import java.io.IOException;

public class GhApiClient extends RestClient{

    private final GhApi ghApi;

    public GhApiClient() {
        super("https://api.github.com");
        this.ghApi = retrofit.create(GhApi.class);
    }

    @Step("Get issue state by number: {issueNumber}")
    public String getIssueState(String issueNumber) throws IOException {
        JsonNode responseBody = ghApi.issue(
                "Bearer " + System.getenv("GH_TOKEN"),
                issueNumber
        ).execute().body();
        return responseBody.get("state").asText();
    }
}
