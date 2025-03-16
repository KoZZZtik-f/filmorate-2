package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyFriendsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.mapper.Mappers;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(int userId, int friendId) {
        // user_id < friend_id (В сервис-классе уже сделано так)
        final String sql = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";

        try {
            jdbcTemplate.update(sql, userId, friendId);
        } catch (DuplicateKeyException e) {
            throw new AlreadyFriendsException();
        } catch (DataIntegrityViolationException e) {
            throw new UserNotFoundException(String.format("One of these id's (%d, %d) is unknown", userId, friendId));
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        //Подавать в виде (userId < friendId)
        final String sql = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";

        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        final String sql = "SELECT u.id, u.name, u.login, u.email, u.birthday " +
                "FROM users u " +
                "JOIN friendships f ON (u.id = f.friend_id AND f.user_id = ?) " +
                "OR (u.id = f.user_id AND f.friend_id = ?)";

        return jdbcTemplate.query(sql, Mappers.getUserRowMapper(), userId, userId);
    }

    @Override
    public Set<User> getCommonFriends(int id, int otherId) {
        final String sql = "SELECT u.id, u.name, u.login, u.email, u.birthday " +
                "FROM users u " +
                "JOIN friendships f1 ON f1.friend_id = u.id " +
                "JOIN friendships f2 ON f2.friend_id = u.id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";

        List<User> commonFriends = jdbcTemplate.query(sql, Mappers.getUserRowMapper(), id, otherId);
        return Set.copyOf(commonFriends);
    }
}
