package guru.qa.niffler.api.client;

import guru.qa.niffler.api.service.UserApi;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;

public class UserApiClient extends RestClient{

    private final UserApi userApi;

    public UserApiClient() {
        super("http://127.0.0.1:8089");
        this.userApi = retrofit.create(UserApi.class);
    }

    @Step("Обновить данные юзера POST updateUserInfo")
    public UserJson updateUserInfo(UserJson user) throws IOException {
        return userApi.updateUserInfo(user).execute().body();
    }

    @Step("Пполучить данные юзера GET currentUser")
    public UserJson currentUser(String username) throws IOException {
        return userApi.currentUser(username).execute().body();
    }

    @Step("Пполучить данные юзера GET allUsers")
    public List<UserJson> allUsers(String username) throws IOException {
        return userApi.allUsers(username).execute().body();
    }
}
