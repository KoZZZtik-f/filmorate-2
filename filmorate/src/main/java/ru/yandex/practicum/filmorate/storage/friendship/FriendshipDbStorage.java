package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyFriendsException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage{

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void addFriend(int userId, int friendId) {

        final String sql = "insert into friendships (user_id, friend_id) values (least(?, ?), greatest(?, ?))";
        try {
            jdbcTemplate.update(sql, userId, friendId);
        } catch (Throwable e) {
            log.debug(e.getMessage());
            throw new AlreadyFriendsException(String.format("Пользователи %d и %d уже являются друзьями",
                    userId, friendId));
        }

    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        final String sql = "delete from friendships where user_id = ? and friend_id = ?";

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
