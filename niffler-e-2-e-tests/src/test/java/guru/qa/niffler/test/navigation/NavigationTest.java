package guru.qa.niffler.test.navigation;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.NavigationPage;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class NavigationTest extends BaseWebTest {

    private LoginPage loginPage = new LoginPage();

    private NavigationPage nav = new NavigationPage();
    @DisplayName("Переход на страницу друзей")
    @DBUser
    @Test
    void goToFriendsPage(AuthUserEntity user) {
        loginPage.signIn(user.getUsername(), user.getPassword());
        nav
                .goToFriends()
                .verifyOnFriendsPage();
    }

    @DisplayName("Переход на страницу людей")
    @DBUser
    @Test
    void goToAllPeoplePage(AuthUserEntity user) {
        loginPage.signIn(user.getUsername(), user.getPassword());
        nav
                .goToAllPeople()
                .checkPeoplePageURL();
    }

    @DisplayName("Переход на страницу профиля")
    @DBUser
    @Test
    void goToProfilePage(AuthUserEntity user) {
        loginPage.signIn(user.getUsername(), user.getPassword());
        nav
                .goToProfile()
                .checkingProfilePage();
    }

    @DisplayName("Переход на главную страницу")
    @DBUser
    @Test
    void goToMainPage(AuthUserEntity user) {
        loginPage.signIn(user.getUsername(), user.getPassword());
        nav.goToProfile()
                .checkingProfilePage();
        nav.goToMain()
                .checkingMainPage();
    }

    @DisplayName("Проверка отображения элементов на странице профиля")
    @DBUser
    @Test
    void shouldBeProfileElementsInPage(AuthUserEntity user) {
        loginPage.signIn(user.getUsername(), user.getPassword());
        nav.goToProfile()
                .checkingProfilePage();
        nav.goToProfile()
                .profilePageShouldBeVisibleAfterLogin();
    }
}
