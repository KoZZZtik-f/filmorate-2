package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserStorage userStorage;

    @Mock
    private FriendshipStorage friendshipStorage;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));

        user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1995, 5, 5));
    }

    @Test
    void createUser_shouldReturnCreatedUser() {
        when(userStorage.createUser(any(User.class))).thenReturn(user1);

        User createdUser = userService.createUser(user1);

        assertEquals(user1, createdUser);
        verify(userStorage, times(1)).createUser(user1);
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() {
        when(userStorage.updateUser(any(User.class))).thenReturn(user1);

        User updatedUser = userService.updateUser(user1);

        assertEquals(user1, updatedUser);
        verify(userStorage, times(1)).updateUser(user1);
    }

    @Test
    void getUserById_shouldReturnUser() {
        when(userStorage.getUserById(1)).thenReturn(user1);

        User foundUser = userService.getUserById(1);

        assertEquals(user1, foundUser);
        verify(userStorage, times(1)).getUserById(1);
    }

    @Test
    void getUserById_shouldThrowNotFoundException() {
        when(userStorage.getUserById(3)).thenThrow(new NotFoundException("Пользователь с id 3 не найден"));

        assertThrows(NotFoundException.class, () -> userService.getUserById(3));
    }

    @Test
    void addFriend_shouldCallFriendshipStorage() {
        userService.addFriend(1, 2);

        verify(friendshipStorage, times(1)).addFriend(1, 2);
    }

    @Test
    void removeFriend_shouldCallFriendshipStorage() {
        userService.removeFriend(1, 2);

        verify(friendshipStorage, times(1)).deleteFriend(1, 2);
    }

    @Test
    void getUserAllFriends_shouldReturnFriendsList() {
        when(friendshipStorage.getFriends(1)).thenReturn(List.of(user2));

        List<User> friends = userService.getUserAllFriends(1);

        assertEquals(1, friends.size());
        assertEquals(user2, friends.get(0));
        verify(friendshipStorage, times(1)).getFriends(1);
    }

    @Test
    void getCommonFriends_shouldReturnCommonFriendsList() {
        when(friendshipStorage.getCommonFriends(1, 2)).thenReturn(Set.of(user2));

        var commonFriends = userService.getCommonFriends(1, 2);

        assertEquals(1, commonFriends.size());
        assertTrue(commonFriends.contains(user2));
        verify(friendshipStorage, times(1)).getCommonFriends(1, 2);
    }

    @Test
    void checkUserNotFound_shouldThrowExceptionIfUserDoesNotExist() {
        when(userStorage.contains(3)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.checkUserNotFound(3));
    }

    @Test
    void checkUserNotFound_shouldNotThrowExceptionIfUserExists() {
        when(userStorage.contains(1)).thenReturn(true);

        assertDoesNotThrow(() -> userService.checkUserNotFound(1));
    }
}
