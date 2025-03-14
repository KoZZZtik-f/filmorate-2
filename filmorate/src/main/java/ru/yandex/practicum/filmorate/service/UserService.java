package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    private static final String NOT_FOUND_MESSAGE = "Пользователь с id %d не найден";
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;


    public User createUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(Integer userId, Integer friendId) {
        int minId = Math.min(userId, friendId);
        int maxId = Math.max(userId, friendId);

        friendshipStorage.addFriend(minId, maxId);
    }

    public void removeFriend(Integer id, Integer friendId) {
        int minId = Math.min(id, friendId);
        int maxId = Math.max(id, friendId);

        friendshipStorage.deleteFriend(minId, maxId);
    }

    public List<User> getUserAllFriends(Integer id) {

        return friendshipStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(Integer id, Integer otherId) {
        Set<User> res = friendshipStorage.getCommonFriends(id, otherId);

        return res;
    }



    public void checkUserNotFound(Integer...  ids) {
        for (Integer id : ids) {
            if (!userStorage.contains(id)) {
                throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, id));
            }
        }
    }
    public void checkUserNotFound(User... users) {
        for (User user : users) {
            checkUserNotFound(user.getId());
        }
    }

}
