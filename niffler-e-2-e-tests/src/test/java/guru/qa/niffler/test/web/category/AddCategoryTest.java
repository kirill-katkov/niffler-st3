package guru.qa.niffler.test.web.category;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.NavigationPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.test.web.BaseWebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class AddCategoryTest extends BaseWebTest {

    private final LoginPage login = new LoginPage();
    private final NavigationPage nav = new NavigationPage();

    private final ProfilePage profile = new ProfilePage();

    @ApiLogin(
            user = @GenerateUser
    )
    @Test
    void addCategory() {
        String category = "Курсы";
        login.openMain();
        nav.goToProfile();
        profile.enterCategory(category);
        profile.create();
        profile.checkAddedCategory(category);
    }
}
