package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface FriendshipStorage {

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    Set<User> getFriends(int userId);

}
