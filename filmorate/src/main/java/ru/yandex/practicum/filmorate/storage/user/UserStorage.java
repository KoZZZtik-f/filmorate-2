package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    User getUserById(int id);

    User removeUserById(int id);

    Collection<User> getAllUsers();

    Collection<User> getUserFriends(int userId);

    Boolean contains(int id);

    Boolean contains(User user);


}
