package guru.qa.niffler.test.web.navigation;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.NavigationPage;
import guru.qa.niffler.test.web.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class NavigationTest extends BaseWebTest {

    private final LoginPage loginPage = new LoginPage();

    private final NavigationPage nav = new NavigationPage();
    @DisplayName("Переход на страницу друзей")
    @DBUser
    @ApiLogin
    @Test
    void goToFriendsPage() {
        loginPage.openMain();
        nav
                .goToFriends()
                .verifyOnFriendsPage();
    }

    @DisplayName("Переход на страницу людей")
    @DBUser
    @ApiLogin
    @Test
    void goToAllPeoplePage() {
        loginPage.openMain();
        nav.goToAllPeople()
                .checkPeoplePageURL();
    }

    @DisplayName("Переход на страницу профиля")
    @DBUser
    @ApiLogin
    @Test
    void goToProfilePage() {
        loginPage.openMain();
        nav.goToProfile()
                .checkingProfilePage();
    }

    @DisplayName("Переход на главную страницу")
    @DBUser
    @ApiLogin
    @Test
    void goToMainPage() {
        loginPage.openMain();
        nav.goToProfile()
                .checkingProfilePage();
        nav.goToMain()
                .checkingMainPage();
    }

    @DisplayName("Проверка отображения элементов на странице профиля")
    @DBUser
    @ApiLogin
    @Test
    void shouldBeProfileElementsInPage() {
        loginPage.openMain();
        nav.goToProfile()
                .checkingProfilePage();
        nav.goToProfile()
                .profilePageShouldBeVisibleAfterLogin();
    }
}
