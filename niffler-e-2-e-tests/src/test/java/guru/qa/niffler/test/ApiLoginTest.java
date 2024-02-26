package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.ContextHolderExtension;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith({ContextHolderExtension.class, ApiLoginExtension.class})
public class ApiLoginTest extends BaseWebTest{

    @ApiLogin(username = "duck", password = "12345")
    @Test
    void successApiLoginTest() {
       open(MainPage.URL, MainPage.class).waitUntilLoaded();
    }
}
