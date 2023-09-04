package guru.qa.niffler.test.test2;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EditProfileWebTest extends BaseWebTest {

    private final static String user = "profi53223";
    private final static String password = "12345";
    private final static String firstName = "Oleg";
    private final static String surname = "Gazmanov";
    private static AuthUserEntity currentUser;
    @BeforeAll
    static void setUp() {
        // Здесь создаем объект currentUser
        currentUser = new AuthUserEntity();
        currentUser.setUsername(user);
        currentUser.setPassword(password);
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(currentUser.getUsername());
        $("input[name='password']").setValue(currentUser.getPassword());
        $("button[type='submit']").click();
        $(".main-content__section-stats").shouldBe(visible);
    }

    @DBUser(username = user,
            password = password)
    @Test
    void profileUpdatedMessageShouldBeVisible() {
        $x("//a[@href='/profile']").click();
        $x("//input[@name='firstname']").setValue(firstName);
        $x("//input[@name='surname']").setValue(surname);
        $x("//button[text()='Submit']").scrollTo().click();
        $x("//div[text()='Profile updated!']").shouldBe(visible);
    }
}
