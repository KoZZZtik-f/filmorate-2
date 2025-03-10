package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int filmId, int userId) {
        final String sql = "insert into likes (film_id, user_id) values (?, ?)";

        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        final String sql = "delete from likes where film_id = ? and user_id = ?";

        Integer updatedRowsCount = jdbcTemplate.update(sql, filmId, userId);
        if (updatedRowsCount == 0) {
            throw new NotFoundException(String.format("Either film (%d) or user (%d) not exist.", filmId, userId) );
        }
    }
}
