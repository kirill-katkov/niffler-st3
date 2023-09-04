package guru.qa.niffler.test.authentication;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.NavigationPage;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class LogoutTest extends BaseWebTest {

    private LoginPage loginPage = new LoginPage();

    private NavigationPage nav = new NavigationPage();
    @DisplayName("Разлогин на главной странице")
    @DBUser
    @Test
    void logout(AuthUserEntity user) {
        loginPage.signIn(user.getUsername(), user.getPassword());
        nav.logout();
        loginPage.checkWelcome();
    }
}
