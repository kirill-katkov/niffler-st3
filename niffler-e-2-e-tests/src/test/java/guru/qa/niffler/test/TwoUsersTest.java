package guru.qa.niffler.test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static io.qameta.allure.Allure.step;

public class TwoUsersTest {

    @BeforeEach
    void doLogin(@User(userType = INVITATION_SENT) UserJson user1, @User(userType = INVITATION_RECEIVED) UserJson user2) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user1.getUsername());
        $("input[name='password']").setValue(user1.getPassword());
        $("button[type='submit']").click();
    }

    @Test
    @AllureId("106")
    @DisplayName("Проверка использующая сразу двух пользователей разного типа")
    void invitationShouldBeDisplayedInTableTwoUsers(@User(userType = INVITATION_SENT) UserJson user1, @User(userType = INVITATION_RECEIVED) UserJson user2) throws InterruptedException {

        step("Открыть страницу \"friends\"", () ->
                $(Selectors.byAttribute("href", "/people")).click()
        );

        step("Количество строк со статусом \"Pending invitation\" должно равняться 1", () ->
                $(".people-content").$("table").shouldBe(Condition.visible)
                        .$("tbody")
                        .$$("td")
                        .filterBy(text("Pending invitation"))
                        .shouldHave(CollectionCondition.size(1))
        );
    }
}
