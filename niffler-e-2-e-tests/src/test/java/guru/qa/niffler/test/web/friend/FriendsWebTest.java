package guru.qa.niffler.test.web.friend;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.NavigationPage;
import guru.qa.niffler.test.web.BaseWebTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;


public class FriendsWebTest extends BaseWebTest {
    private LoginPage loginPage = new LoginPage();
    private NavigationPage navigationPage = new NavigationPage();
    private FriendsPage friendsPage = new FriendsPage();

    @BeforeEach
    void doLogin(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        loginPage.signIn(userForTest);
    }

    @DisplayName("Друг должен отображаться в таблице")
    @Test
    void friendShouldBeDisplayedInTable(@User(userType = WITH_FRIENDS) UserJson anotherUserForTest) {
        friendsPage = navigationPage.goToFriends();
        friendsPage.verifyListExists();
        friendsPage.verifyFriendshipWithUser(anotherUserForTest.getUsername());
    }


}
