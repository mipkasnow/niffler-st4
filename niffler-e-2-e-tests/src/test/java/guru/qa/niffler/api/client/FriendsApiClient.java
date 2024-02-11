package guru.qa.niffler.api.client;

import guru.qa.niffler.api.service.FriendsApi;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;

public class FriendsApiClient extends RestClient{

    private final FriendsApi friendsApi;

    public FriendsApiClient() {
        super("http://127.0.0.1:8089");
        this.friendsApi = retrofit.create(FriendsApi.class);
    }

    @Step("Получить список друзей GET friends юзера {username}")
    public List<UserJson> friends(String username, boolean includePending) throws IOException {
        return friendsApi.friends(username, includePending).execute().body();
    }

    @Step("Получить список приглашений GET invitations юзера {username}")
    public List<UserJson> invitations(String username) throws IOException {
        return friendsApi.invitations(username).execute().body();
    }

    @Step("Отклонить приглашение POST declineInvitation юзеру {username}")
    public List<UserJson> declineInvitation(String username, FriendJson invitation) throws IOException {
        return friendsApi.declineInvitation(username, invitation).execute().body();
    }

    @Step("Принять приглашение POST acceptInvitation юзеру {username}")
    public List<UserJson> acceptInvitation(String username, FriendJson invitation) throws IOException {
        return friendsApi.acceptInvitation(username, invitation).execute().body();
    }

    @Step("Добавить друга POST addFriend юзеру {username}")
    public UserJson addFriend(String username, FriendJson friend) throws IOException {
        return friendsApi.addFriend(username, friend).execute().body();
    }

    @Step("Удалить друга DELETE removeFriend юзеру {username}")
    public List<UserJson> removeFriend(String username, String friendUsername) throws IOException {
        return friendsApi.removeFriend(username, friendUsername).execute().body();
    }
}
