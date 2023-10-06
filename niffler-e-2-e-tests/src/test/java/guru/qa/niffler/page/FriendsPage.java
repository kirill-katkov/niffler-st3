package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.WebDriverRunner;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FriendsPage extends BasePage {

    public FriendsPage verifyListExists() {
        step("Verify that the list exists", () -> {
            $$(".abstract-table tbody tr").shouldHave(sizeGreaterThan(0));
        });
        return this;
    }

    public FriendsPage verifyFriendshipWithUser(String username) {
        step("Verify friendship with user: " + username, () -> {
            $(byText(username)).shouldBe(visible);
        });
        return this;
    }

    public FriendsPage verifyInvitationReceived() {
        step("Verify invitation received (Friends page)", () -> {
            $("div[data-tooltip-id='submit-invitation']").shouldBe(visible);
        });
        return this;
    }

    public FriendsPage verifyOnFriendsPage() {
        step("Verify that the current page is Friends page", () -> {
            assertEquals(CFG.baseUrl() + "/friends", WebDriverRunner.url());
        });
        return this;
    }

    public FriendsPage checkingThatListExist() {
        step("Checking that the list exists", () ->
                $$(".abstract-table tbody tr").
                        shouldBe(CollectionCondition.sizeGreaterThan(0)));
        return this;
    }


    public FriendsPage checkingYouFriends(String username) {
        step("Checking that you are friends", () ->
                $(byText(username)).shouldBe(visible));
        return this;
    }


    public FriendsPage checkingInvitationReceived() {
        step("Checking invitation received (page Friends)", () ->
                $("div[data-tooltip-id='submit-invitation']").shouldBe(visible));
        return this;
    }

    public FriendsPage checkingFreindsPage() {
        step("Check friends page", () ->
                assertEquals(WebDriverRunner.url(), CFG.baseUrl() + "/friends"));
        return this;
    }
}
