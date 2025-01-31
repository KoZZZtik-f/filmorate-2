package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
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
        return map.put(user.getId(), user);
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
}
