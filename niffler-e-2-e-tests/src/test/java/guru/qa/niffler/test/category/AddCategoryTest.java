package guru.qa.niffler.test.category;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.NavigationPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class AddCategoryTest extends BaseWebTest {

    private LoginPage login = new LoginPage();
    private NavigationPage nav = new NavigationPage();

    private ProfilePage profile = new ProfilePage();
    @DisplayName("Добавление категории")
    @DBUser
    @Test
    void addCategory(AuthUserEntity user) {
        String category = "Курсы";
        login.signIn(user.getUsername(), user.getPassword());
        nav.goToProfile();
        profile.enterCategory(category);
        profile.create();
        profile.checkAddedCategory(category);
    }
}
