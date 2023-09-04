package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.NavigationPage;
import guru.qa.niffler.page.PeoplePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;

public class InvitationReceivedWebTest extends BaseWebTest {

    private LoginPage loginPage = new LoginPage();
    private NavigationPage navigationPage = new NavigationPage();
    private FriendsPage friendsPage = new FriendsPage();
    private PeoplePage peoplePage = new PeoplePage();

    @BeforeEach
    void loginWithInvitationReceivedUser(@User(userType = INVITATION_RECEIVED) UserJson userForTest) {
        loginPage.signIn(userForTest);
    }

    @Test
    void verifyInvitationReceivedInFriendsTable() {
        friendsPage = navigationPage
                .goToFriends();
        friendsPage.verifyListExists();
        friendsPage.verifyInvitationReceived();
    }

    @Test
    void verifyInvitationReceivedInSubmitInvitedTable() {
        peoplePage = navigationPage
                .goToAllPeople();
        peoplePage.checkSubmitInvitationButtonIsDisplayed();
    }

    @Test
    void verifyInvitationReceivedInSubmitDeclinedTable() {
        peoplePage = navigationPage
                .goToAllPeople();
        peoplePage.checkDeclineInvitationButtonIsDisplayed();
    }
}
