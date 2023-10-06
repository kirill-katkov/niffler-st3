package guru.qa.niffler.test.web.friend;

import guru.qa.niffler.jupiter.annotation.*;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.NavigationPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.test.web.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.annotation.GeneratedUser.Selector.NESTED;
import static guru.qa.niffler.jupiter.annotation.GeneratedUser.Selector.OUTER;


public class FriendsWebTest extends BaseWebTest {
    private NavigationPage navigationPage = new NavigationPage();
    private FriendsPage friendsPage = new FriendsPage();
    private PeoplePage peoplePage = new PeoplePage();

    @ApiLogin(
            user = @GenerateUser(
                    friends = @Friend
            )
    )
    @GenerateUser
    @DisplayName("Verify friend presence in the table")
    @Test
    void friendShouldBePresentInTable(@GeneratedUser(selector = NESTED) UserJson userForTest,
                                      @GeneratedUser(selector = OUTER) UserJson another) {
        open(CFG.nifflerFrontUrl() + "/main");
        friendsPage = navigationPage.goToFriends();
        friendsPage.checkingThatListExist();
        friendsPage.checkingYouFriends(userForTest.getFriends().get(0).getUsername());
    }

    @ApiLogin(
            user = @GenerateUser(
                    incomeInvitations = @IncomeInvitation
            )
    )
    @DisplayName("Verify income invitation presence in the table")
    @Test
    void incomeInvitationShouldBePresentInTable(@GeneratedUser(selector = NESTED) UserJson userForTest) {
        open(CFG.nifflerFrontUrl() + "/main");
        peoplePage = navigationPage.goToAllPeople();
        peoplePage.checkingAllPeoplePage();
        peoplePage.checkingPendingInvitation();
    }

    @ApiLogin(
            user = @GenerateUser(
                    outcomeInvitations = @OutcomeInvitation
            )
    )
    @DisplayName("Verify outcome invitation presence in the table")
    @Test
    void outcomeInvitationShouldBePresentInTable() {
        open(CFG.nifflerFrontUrl() + "/main");
        friendsPage = navigationPage.goToFriends();
        friendsPage.checkingThatListExist();
        friendsPage.checkingInvitationReceived();
    }
}