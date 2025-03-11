package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.Mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> getAllGenres() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, Mappers.getGenreRowMapper());
    }

    @Override
    public Genre getGenreById(Integer id) {
        String sql = "SELECT * FROM genres WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Mappers.getGenreRowMapper(), id);
    }

    @Override
    public Collection<Genre> getGenresByFilmId(Integer filmId) {
        String sql = "SELECT genre.id, genre.name FROM genres genre " +
                "JOIN films film ON film.genre_id = genre.id " +
                "WHERE film.id = ?";
        return jdbcTemplate.query(sql, Mappers.getGenreRowMapper(), filmId);
    }
}
