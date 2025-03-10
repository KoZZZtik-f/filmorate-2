package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        user.setId(simpleJdbcInsert.executeAndReturnKey(parameterSource).intValue());

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

        try {
            return jdbcTemplate.queryForObject(sql, Mappers.getUserRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public User removeUserById(int id) {
        return null;
    }

    @Override
    public Collection<User> getAllUsers() {
        final String sql = "SELECT id, name, login, email, birthday FROM users";

        return jdbcTemplate.query(sql, Mappers.getUserRowMapper());
    }

    @Override
    public Collection<User> getUserFriends(int userId) {
        final String sql = "select * from users where id in (select friend_id from friendship where )";

        return jdbcTemplate.query(sql, Mappers.getUserRowMapper(), userId, userId, userId);
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
