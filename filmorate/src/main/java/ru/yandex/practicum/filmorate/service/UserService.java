package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private static int lastId;


    public User createUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getId() == null) {
            user.setId(++lastId);
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

    public void addFriend(Integer id, Integer friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.removeUserById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public void removeFriend(Integer id, Integer friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.removeUserById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);

        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public Set<Integer> getAllFriends(Integer id) {
        return userStorage.getUserById(id).getFriends();
    }

    public Set<Integer> getCommonFriends(Integer id, Integer otherId) {
        Set<Integer> friends1 = userStorage.getUserById(id).getFriends();
        Set<Integer> friends2 = userStorage.getUserById(otherId).getFriends();

        Set<Integer> intersection = friends1.stream()
                .filter(friends2::contains)
                .collect(Collectors.toSet());
        return intersection;
    }

}
