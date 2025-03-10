package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.Mappers;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {
        final String sql = "INSERT INTO films (id, name, description, release_date, duration) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration().getSeconds());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        final String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ? WHERE id = ?";
        Integer updatedCount = jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration().getSeconds(), film.getId());
        if (updatedCount > 0) {
            return film;
        } else {
            throw new FilmNotFoundException(film.getId());
        }
    }

    @Override
    public Film getFilmById(int id) {
        final String sql = "SELECT * FROM films WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Mappers.getFilmRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(id);
        }
    }

    @Override
    public void removeFilmById(int id) {
        final String sql = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        final String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, Mappers.getFilmRowMapper());
    }

    @Override
    public Collection<Film> getMostPopularFilms(Integer count) {
        final String sql = "SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
                "COUNT(l.user_id) AS likes_cnt " +
                "FROM films f LEFT JOIN likes l ON f.id = l.film_id " +
                "GROUP BY f.id " +
                "ORDER BY likes_cnt DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, Mappers.getFilmRowMapper(), count);
    }

    @Override
    public boolean contains(Film film) {
        return contains(film.getId());
    }

    @Override
    public boolean contains(Integer id) {
        final String sql = "SELECT COUNT(*) FROM films WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}