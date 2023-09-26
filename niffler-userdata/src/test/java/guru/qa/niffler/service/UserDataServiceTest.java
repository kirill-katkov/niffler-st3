package guru.qa.niffler.service;

import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.ex.NotFoundException;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static guru.qa.niffler.model.FriendState.FRIEND;
import static guru.qa.niffler.model.FriendState.INVITE_SENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDataServiceTest {

    private final UUID mainTestUserUuid = UUID.randomUUID();
    private final String mainTestUserName = "dima";
    private final UUID secondTestUserUuid = UUID.randomUUID();
    private final String secondTestUserName = "barsik";
    private final UUID thirdTestUserUuid = UUID.randomUUID();
    private final String thirdTestUserName = "emma";
    private final String notExistingUser = "not_existing_user";


//    String mainTestUser1 = "valentin";
//        String secondTestUserName = "pizzly";
    private UserDataService testedObject;
    private UserEntity mainTestUser;
    private UserEntity secondTestUser;
    private UserEntity thirdTestUser;

    static Stream<Arguments> friendsShouldReturnDifferentListsBasedOnIncludePendingParam() {
        return Stream.of(
                Arguments.of(true, List.of(INVITE_SENT, FRIEND)),
                Arguments.of(false, List.of(FRIEND))
        );
    }

    @BeforeEach
    void init() {
        mainTestUser = new UserEntity();
        mainTestUser.setId(mainTestUserUuid);
        mainTestUser.setUsername(mainTestUserName);
        mainTestUser.setCurrency(CurrencyValues.RUB);

        secondTestUser = new UserEntity();
        secondTestUser.setId(secondTestUserUuid);
        secondTestUser.setUsername(secondTestUserName);
        secondTestUser.setCurrency(CurrencyValues.RUB);

        thirdTestUser = new UserEntity();
        thirdTestUser.setId(thirdTestUserUuid);
        thirdTestUser.setUsername(thirdTestUserName);
        thirdTestUser.setCurrency(CurrencyValues.RUB);
    }

    @ValueSource(strings = {"photo", ""})
    @ParameterizedTest
    void userShouldBeUpdated(String photo, @Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(mainTestUserName)))
                .thenReturn(mainTestUser);

        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(answer -> answer.getArguments()[0]);

        testedObject = new UserDataService(userRepository);

        final String photoForTest = photo.equals("") ? null : photo;

        final UserJson toBeUpdated = new UserJson();
        toBeUpdated.setUsername(mainTestUserName);
        toBeUpdated.setFirstname("Test");
        toBeUpdated.setSurname("TestSurname");
        toBeUpdated.setCurrency(CurrencyValues.USD);
        toBeUpdated.setPhoto(photoForTest);
        final UserJson result = testedObject.update(toBeUpdated);
        assertEquals(mainTestUserUuid, result.getId());
        assertEquals("Test", result.getFirstname());
        assertEquals("TestSurname", result.getSurname());
        assertEquals(CurrencyValues.USD, result.getCurrency());
        assertEquals(photoForTest, result.getPhoto());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void getRequiredUserShouldThrowNotFoundExceptionIfUserNotFound(@Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(notExistingUser)))
                .thenReturn(null);

        testedObject = new UserDataService(userRepository);

        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> testedObject.getRequiredUser(notExistingUser));
        assertEquals(
                "Can`t find user by username: " + notExistingUser,
                exception.getMessage()
        );
    }

    @Test
    void allUsersShouldReturnCorrectUsersList(@Mock UserRepository userRepository) {
        when(userRepository.findByUsernameNot(eq(mainTestUserName)))
                .thenReturn(getMockUsersMappingFromDb());

        testedObject = new UserDataService(userRepository);

        List<UserJson> users = testedObject.allUsers(mainTestUserName);
        assertEquals(2, users.size());
        final UserJson invitation = users.stream()
                .filter(u -> u.getFriendState() == INVITE_SENT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Friend with state INVITE_SENT not found"));

        final UserJson friend = users.stream()
                .filter(u -> u.getFriendState() == FRIEND)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Friend with state FRIEND not found"));


        assertEquals(secondTestUserName, invitation.getUsername());
        assertEquals(thirdTestUserName, friend.getUsername());
    }

    @MethodSource
    @ParameterizedTest
    void friendsShouldReturnDifferentListsBasedOnIncludePendingParam(boolean includePending,
                                                                     List<FriendState> expectedStates,
                                                                     @Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(mainTestUserName)))
                .thenReturn(enrichTestUser());

        testedObject = new UserDataService(userRepository);
        final List<UserJson> result = testedObject.friends(mainTestUserName, includePending);
        assertEquals(expectedStates.size(), result.size());

        assertTrue(result.stream()
                .map(UserJson::getFriendState)
                .toList()
                .containsAll(expectedStates));
    }

    private UserEntity enrichTestUser() {
        mainTestUser.addFriends(true, secondTestUser);
        secondTestUser.addInvites(mainTestUser);

        mainTestUser.addFriends(false, thirdTestUser);
        thirdTestUser.addFriends(false, mainTestUser);
        return mainTestUser;
    }


    private List<UserEntity> getMockUsersMappingFromDb() {
        mainTestUser.addFriends(true, secondTestUser);
        secondTestUser.addInvites(mainTestUser);

        mainTestUser.addFriends(false, thirdTestUser);
        thirdTestUser.addFriends(false, mainTestUser);

        return List.of(secondTestUser, thirdTestUser);
    }

    // Тестирование метода getCurrentUser для существующего пользователя:
    @Test
    void getCurrentUserShouldReturnCorrectUser(@Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(mainTestUserName)))
                .thenReturn(mainTestUser);

        testedObject = new UserDataService(userRepository);

        UserJson result = testedObject.getCurrentUser(mainTestUserName);
        assertEquals(mainTestUserName, result.getUsername());
        assertEquals(mainTestUser.getCurrency(), result.getCurrency());
    }

    // Тестирование метода getCurrentUser для несуществующего пользователя:
    @Test
    void getCurrentUserShouldThrowNotFoundExceptionForNonExistingUser(@Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(notExistingUser)))
                .thenReturn(null);

        testedObject = new UserDataService(userRepository);

        assertThrows(NotFoundException.class, () -> testedObject.getCurrentUser(notExistingUser));
    }

    // Тестирование метода acceptInvitation для несуществующего пользователя:
    @Test
    void acceptInvitationShouldThrowNotFoundExceptionForNonExistingUser(@Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(notExistingUser)))
                .thenReturn(null);

        testedObject = new UserDataService(userRepository);

        FriendJson friendJson = new FriendJson();
        friendJson.setUsername("someFriend");

        assertThrows(NotFoundException.class, () -> testedObject.acceptInvitation(notExistingUser, friendJson));
    }

    // Тестирование метода acceptInvitation для существующего пользователя:
    @Test
    void acceptInvitationShouldAcceptInvitationAndReturnFriends(@Mock UserRepository userRepository) {
        // Подготовка данных: создадим пользователей и добавим приглашение
        when(userRepository.findByUsername(eq(mainTestUserName)))
                .thenReturn(mainTestUser);
        when(userRepository.findByUsername(eq(secondTestUserName)))
                .thenReturn(secondTestUser);

        // Предположим, что у mainTestUser есть приглашение от secondTestUser
        mainTestUser.addInvites(secondTestUser);

        testedObject = new UserDataService(userRepository);

        FriendJson friendJson = new FriendJson();
        friendJson.setUsername(secondTestUserName);

        List<UserJson> friends = testedObject.acceptInvitation(mainTestUserName, friendJson);
        assertEquals(1, friends.size());
        assertEquals(FriendState.FRIEND, friends.get(0).getFriendState());
    }

    // Тестирование метода addFriend для добавления друга:
    @Test
    void addFriendShouldAddFriendAndReturnUpdatedUser(@Mock UserRepository userRepository) {
        // Подготовка данных: создаем и сохраняем друга
        FriendJson friendToAdd = new FriendJson();
        friendToAdd.setUsername("newFriend");

        UserEntity friendEntity = new UserEntity();
        friendEntity.setUsername(friendToAdd.getUsername());

        when(userRepository.findByUsername(eq(mainTestUserName)))
                .thenReturn(mainTestUser);
        when(userRepository.findByUsername(eq(friendToAdd.getUsername())))
                .thenReturn(friendEntity);  // Возвращаем друга по имени "newFriend"
        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(new UserEntity());

        testedObject = new UserDataService(userRepository);

        UserJson updatedUser = testedObject.addFriend(mainTestUserName, friendToAdd);

        assertEquals(FriendState.INVITE_SENT, updatedUser.getFriendState());
    }

    // Тестирование метода removeFriend для существующего пользователя:
    @Test
    void removeFriendShouldRemoveFriendAndReturnUpdatedFriendsList(@Mock UserRepository userRepository) {
        testedObject = new UserDataService(userRepository);
        FriendJson friendToRemove = new FriendJson();
        friendToRemove.setUsername("someFriend");

        UserEntity friendEntity = new UserEntity();
        friendEntity.setUsername(friendToRemove.getUsername());

        when(userRepository.findByUsername(eq(mainTestUserName)))
                .thenReturn(mainTestUser);
        when(userRepository.findByUsername(eq(friendToRemove.getUsername())))
                .thenReturn(friendEntity);  // Возвращаем друга по имени "someFriend"

        testedObject = new UserDataService(userRepository);

        List<UserJson> friends = testedObject.removeFriend(mainTestUserName, friendToRemove.getUsername());

        // Проверка, что mainTestUser больше не является другом "someFriend"
        assertFalse(mainTestUser.getFriends().contains(friendEntity));
    }
}