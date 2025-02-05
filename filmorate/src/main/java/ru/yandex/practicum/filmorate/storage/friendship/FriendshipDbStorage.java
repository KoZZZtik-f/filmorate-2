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
    public Set<User> getFriends(int userId) {
        throw new RuntimeException("Метод еще в разработке");
    }

}
