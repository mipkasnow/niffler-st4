package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class})
public abstract class BaseWebTest {

    protected final WelcomePage welcomePage = new WelcomePage();
    protected final MainPage mainPage = new MainPage();
    protected final WelcomePage.SignInPage signInPage = new WelcomePage.SignInPage();

    static {
        Configuration.browserSize = "1980x1024";
        Configuration.browser = "firefox";
    }
}
