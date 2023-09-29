package guru.qa.niffler.test.web.authentication;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.test.web.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class LoginTest extends BaseWebTest {

    private LoginPage loginPage = new LoginPage();
    @DisplayName("Главная страница должна быть видимой после авторизации")
    @DBUser
    @Test
    void mainPageShouldBeVisibleAfterLogin(AuthUserEntity user) {
        loginPage.signIn(user.getUsername(), user.getPassword());
    }
}
