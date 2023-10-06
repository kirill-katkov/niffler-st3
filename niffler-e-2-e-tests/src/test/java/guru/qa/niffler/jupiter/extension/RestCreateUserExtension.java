package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.service.AuthServiceClient;
import guru.qa.niffler.api.service.FriendsServiceClient;
import guru.qa.niffler.api.service.RegisterServiceClient;
import guru.qa.niffler.api.context.SessionStorageContext;
import guru.qa.niffler.jupiter.annotation.Friend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.IncomeInvitation;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitation;
import guru.qa.niffler.model.UserJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.utils.FakerUtils.generateRandomUsername;

public class RestCreateUserExtension extends CreateUserExtension {

    private static final String DEFAULT_PASSWORD = "12345";

    private final RegisterServiceClient registerService;
    private final AuthServiceClient authServiceClient;
    private final FriendsServiceClient friendsServiceClient;

    public RestCreateUserExtension() {
        this.registerService = new RegisterServiceClient();
        this.authServiceClient = new AuthServiceClient();
        this.friendsServiceClient = new FriendsServiceClient();
    }

    public RestCreateUserExtension(RegisterServiceClient registerService, AuthServiceClient authServiceClient, FriendsServiceClient friendsServiceClient) {
        this.registerService = registerService;
        this.authServiceClient = authServiceClient;
        this.friendsServiceClient = friendsServiceClient;
    }

    @Override
    protected UserJson createUserForTest(GenerateUser annotation) {
        UserJson userJson = new UserJson();
        String username = generateRandomUsername();
        registerService.register(username, DEFAULT_PASSWORD);
        userJson.setUsername(username);
        userJson.setPassword(DEFAULT_PASSWORD);
        return userJson;
    }

    @Override
    protected List<UserJson> createFriendsIfPresent(GenerateUser annotation, UserJson currentUser) {
        Friend friends = annotation.friends();
        List<UserJson> userJsonList = new ArrayList<>();
        if (friends.handleAnnotation()) {
            for (int i = 0; i < friends.count(); i++) {
                UserJson friend = createUserForTest(annotation);
                addFriend(currentUser, friend);
                acceptInvitation(currentUser, friend);
                userJsonList.add(friend);
            }
        }
        return userJsonList;
    }

    @Override
    protected List<UserJson> createIncomeInvitationsIfPresent(GenerateUser annotation, UserJson currentUser) {
        IncomeInvitation incomeInvitation = annotation.incomeInvitations();
        List<UserJson> userJsonList = new ArrayList<>();
        if (incomeInvitation.handleAnnotation()) {
            for (int i = 0; i < incomeInvitation.count(); i++) {
                UserJson friend = createUserForTest(annotation);
                addFriend(currentUser, friend);
                userJsonList.add(friend);
            }
        }
        return userJsonList;
    }

    @Override
    protected List<UserJson> createOutcomeInvitationsIfPresent(GenerateUser annotation, UserJson currentUser) {
        OutcomeInvitation outcomeInvitation = annotation.outcomeInvitations();
        List<UserJson> userJsonList = new ArrayList<>();
        if (outcomeInvitation.handleAnnotation()) {
            for (int i = 0; i < outcomeInvitation.count(); i++) {
                UserJson friend = createUserForTest(annotation);
                addFriend(friend, currentUser);
                userJsonList.add(friend);
            }
        }
        return userJsonList;
    }

    private void acceptInvitation(UserJson currentUser, UserJson friend) {
        try {
            handleAcceptInvitation(currentUser, friend);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при принятии приглашения", e);
        }
    }

    private void handleAcceptInvitation(UserJson currentUser, UserJson friend) throws IOException {
        authServiceClient.doLogin(friend.getUsername(), DEFAULT_PASSWORD);
        String tokenFriendUser = SessionStorageContext.getInstance().getToken();
        friendsServiceClient.acceptInvitation(tokenFriendUser, currentUser);
    }

    private void addFriend(UserJson currentUser, UserJson friend) {
        try {
            handleAddFriend(currentUser, friend);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при добавлении друга", e);
        }
    }

    private void handleAddFriend(UserJson currentUser, UserJson friend) throws IOException {
        authServiceClient.doLogin(currentUser.getUsername(), DEFAULT_PASSWORD);
        String tokenCurrentUser = SessionStorageContext.getInstance().getToken();
        friendsServiceClient.addFriend(tokenCurrentUser, friend);
    }
}
