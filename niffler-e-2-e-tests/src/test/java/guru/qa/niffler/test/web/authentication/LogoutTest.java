package guru.qa.niffler.test.web.authentication;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.NavigationPage;
import guru.qa.niffler.test.web.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
public class LogoutTest extends BaseWebTest {

    private final LoginPage loginPage = new LoginPage();

    private final NavigationPage nav = new NavigationPage();
    @DisplayName("Разлогин на главной странице")
    @ApiLogin(
            user = @GenerateUser
    )
    @Test
    void logout() {
        loginPage.openMain();
        nav.logout();
        loginPage.checkWelcome();
    }
}