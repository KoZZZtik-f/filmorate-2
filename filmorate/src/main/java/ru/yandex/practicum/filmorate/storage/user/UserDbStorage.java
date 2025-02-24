package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (resultSet, rowNum) -> {
        User user = new User();
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return user;
    };

    @Override
    public User createUser(User user) {
        final String sql = "insert into users (name, login, email, birthday) values (?, ?, ?, ?)";

        jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        return user;
    }

    @Override
    public User updateUser(User user) {
        final String sql = "update users set name = ?, login = ?, email = ?, birthday = ? where id = ?";

        jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User getUserById(int id) {
        final String sql = "select name, login, email, birthday from users where id = ?";

        User user = jdbcTemplate.queryForObject(sql, userRowMapper, id);
        return user;
    }

    @Override
    public User removeUserById(int id) {
        return null;
    }

    @Override
    public Collection<User> getAllUsers() {
        final String sql = "select name, login, email, birthday from users";

        List<User> users = jdbcTemplate.query(sql, userRowMapper);
        return users;
    }

    @Override
    public Collection<User> getUserFriends(int userId) {
        final String sql = "select * from users where id in (select friend_id from friendship where )";

        List<User> friends = jdbcTemplate.query(sql, userRowMapper, userId);
        //TODO
        return friends;
    }


    @Override
    public Boolean contains(int id) {
        return null;
    }

    @Override
    public Boolean contains(User user) {
        return null;
    }
}
