package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.JavascriptExecutor;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MultiUserTest extends BaseWebTest {

    private final static String user = "r1234p";
    private final static String password = "12345";
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

    @Test
    @DBUser(username = user,
            password = password)
    void profilePageShouldBeVisibleAfterLogin() {
        // Находим элемент с классом "header__link" и атрибутом href="/profile" и кликаем на него
        $(".header__link[href='/profile']").click();

        // Находим секцию профиля
        $(".main-content__section").shouldBe(visible);

// Проверяем, что поля Name и Surname существуют и видимы
        $(".form__input[name='firstname']").shouldBe(visible);
        $(".form__input[name='surname']").shouldBe(visible);
        // Проверяем, что поле "Currency" существует и видимо
        $(".form__label:has(div.css-q0ok8z-control)").shouldBe(visible);

// Проверяем наличие кнопки "Submit"
        $(".button[type='submit']").shouldBe(visible);
    }

    @Test
    @DBUser(username = user,
            password = password)
    public void profileSetSettings() {
        // Находим элемент с классом "header__link" и атрибутом href="/profile" и кликаем на него
        $(".header__link[href='/profile']").click();
        // Заполняем поля "Name" и "Surname"
        $("input[name='firstname']").setValue("kirill_super_1");
        $("input[name='surname']").setValue("kirill_super_1");

        $(".css-b62m3t-container").scrollTo().click();

// Ожидание видимости элемента с селектором ".css-uj28k3-Input input" в течение 10 секунд
        SelenideElement inputElement = $(".css-uj28k3-Input input");
        inputElement.shouldBe(visible);

// Установим значение "USD"
        inputElement.setValue("USD");

        $(".css-b62m3t-container").click();
        $("button[type='submit']").click();
    }
}
