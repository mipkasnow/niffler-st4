package guru.qa.niffler.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.interceptor.CodeInterceptor;
import guru.qa.niffler.api.service.AuthApi;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthApiClient extends RestClient {

  private final AuthApi authApi;

  public AuthApiClient() {
    super(
        CFG.authUrl(),
        true,
        new CodeInterceptor()
    );
    authApi = retrofit.create(AuthApi.class);
  }

  public void doLogin(ExtensionContext context, String username, String password) throws Exception {
    authApi.authorize(
        "code",
        "client",
        "openid",
        CFG.frontUrl() + "/authorized",
        ApiLoginExtension.getCodChallenge(context),
        "S256"
    ).execute();

    authApi.login(
        username,
        password,
        ApiLoginExtension.getCsrfToken()
    ).execute();

    JsonNode responseBody = authApi.token(
        "Basic " + new String(Base64.getEncoder().encode("client:secret".getBytes(StandardCharsets.UTF_8))),
        "client",
        "http://127.0.0.1:3000/authorized",
        "authorization_code",
        ApiLoginExtension.getCode(context),
        ApiLoginExtension.getCodeVerifier(context)
    ).execute().body();

    final String token = responseBody.get("id_token").asText();
    ApiLoginExtension.setToken(context, token);
  }
}
