package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.jupiter.Spend;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.User.UserType.WITH_FRIENDS;

public class SpendingWebTest extends BaseWebTest {

    private static final String user = "kirill";

    static {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1980x1024";
    }

    @BeforeEach
    void doLogin(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }

    @Category(
            username = "kirill",
            category = "рыбалка и прочее"
    )

    @Spend(
            username = user,
            description = "рыбалка",
            category = "рыбалка и прочее",
            amount = 14000.00,
            currency = CurrencyValues.RUB
    )
    @Test
    @AllureId("100")
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend,
                                                  @User(userType = WITH_FRIENDS) UserJson userForTest) {
        $(".spendings__content tbody")
                .$$("tr")
                .find(text(createdSpend.getDescription()))
                .$("td")
                .scrollTo()
                .click();

        Allure.step(
                "Delete spending",
                () -> $(byText("Delete selected")).click())
        ;

        Allure.step(
                "Check spendings",
                () -> $(".spendings__content tbody")
                        .$$("tr")
                        .shouldHave(size(0))
        );
    }
}
