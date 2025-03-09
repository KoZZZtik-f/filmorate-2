package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage{

    private final Map<Integer, User> map = new HashMap<>();


    @Override
    public User createUser(User user) {
        if (map.containsKey(user.getId())) {
            throw new RuntimeException();
        }
        map.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        map.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(int id) {
        return map.get(id);
    }

    @Override
    public User removeUserById(int id) {
        return map.remove(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return map.values();
    }

    @Override
    public Collection<User> getUserFriends(int userId) {
        return List.of();
    }

    @Override
    public Boolean contains(int id) {
        return map.containsKey(id);
    }

    @Override
    public Boolean contains(User user) {
        return map.containsKey(user.getId());
    }

}
