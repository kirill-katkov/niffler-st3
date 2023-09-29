package guru.qa.niffler.test.web.invation;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.NavigationPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.test.web.BaseWebTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;

public class InvitationSentWebTest extends BaseWebTest {
    private LoginPage loginPage = new LoginPage();
    private NavigationPage navigationPage = new NavigationPage();
    private PeoplePage peoplePage = new PeoplePage();

    @BeforeEach
    void loginWithInvitationSentUser(@User(userType = INVITATION_SENT) UserJson userForTest) {
        loginPage.signIn(userForTest);
    }
    @DisplayName("Проверка отправленного приглашения в таблице")
    @Test
    void verifyInvitationSentIsDisplayedInTable() {
        peoplePage = navigationPage
                .goToAllPeople();
         peoplePage.checkPendingInvitationIsVisible();
    }
}
