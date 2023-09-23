package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeoplePage extends BasePage {

    public PeoplePage checkSubmitInvitationButtonIsDisplayed() {
        step("Verify that the 'Submit Invitation' button is displayed on the 'All People' page", () ->
                $("div[data-tooltip-id='submit-invitation']").shouldBe(visible));
        return this;
    }

    public PeoplePage checkDeclineInvitationButtonIsDisplayed() {
        step("Verify that the 'Decline Invitation' button is displayed on the 'All People' page", () ->
                $("div[data-tooltip-id='decline-invitation']").shouldBe(visible));
        return this;
    }

    public PeoplePage checkPendingInvitationIsVisible() {
        step("Verify that 'Pending Invitation' text is visible", () ->
                    $$("tr")
                            .filter(Condition.text("Pending invitation"))
                            .shouldHave(CollectionCondition.sizeGreaterThan(0)));

//                $(byText("Pending Invitation")).shouldBe(visible))

        return this;
    }

    public PeoplePage checkPeoplePageURL() {
        step("Verify the URL of the 'All People' page", () ->
                assertEquals(WebDriverRunner.url(), CFG.baseUrl() + "/people"));
        return this;
    }
}
