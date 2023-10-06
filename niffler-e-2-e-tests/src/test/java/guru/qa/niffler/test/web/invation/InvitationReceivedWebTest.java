package guru.qa.niffler.test.web.invation;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.NavigationPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.test.web.BaseWebTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
@Disabled
public class InvitationReceivedWebTest extends BaseWebTest {

    private LoginPage loginPage = new LoginPage();
    private NavigationPage navigationPage = new NavigationPage();
    private FriendsPage friendsPage = new FriendsPage();
    private PeoplePage peoplePage = new PeoplePage();

    @BeforeEach
    void loginWithInvitationReceivedUser(@User(userType = INVITATION_RECEIVED) UserJson userForTest) {
        loginPage.signIn(userForTest);
    }
    @DisplayName("Получение приглашения в таблице друзей")
    @Test
    void verifyInvitationReceivedInFriendsTable() {
        friendsPage = navigationPage
                .goToFriends();
        friendsPage.verifyListExists();
        friendsPage.verifyInvitationReceived();
    }

    @DisplayName("Получение приглашения в таблице принятых приглашений")
    @Test
    void verifyInvitationReceivedInSubmitInvitedTable() {
        peoplePage = navigationPage
                .goToAllPeople();
        peoplePage.checkSubmitInvitationButtonIsDisplayed();
    }

    @DisplayName("Получение приглашения в таблице отклоненных приглашений")
    @Test
    void verifyInvitationReceivedInSubmitDeclinedTable() {
        peoplePage = navigationPage
                .goToAllPeople();
        peoplePage.checkDeclineInvitationButtonIsDisplayed();
    }
}
