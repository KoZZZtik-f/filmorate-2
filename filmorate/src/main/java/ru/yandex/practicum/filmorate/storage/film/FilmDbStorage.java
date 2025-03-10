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

    private final RowMapper<Film> filmRowMapper = (resultSet, rowNum) -> {
        Film film = new Film();
        film.setId(resultSet.getInt("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(Duration.ofSeconds(resultSet.getInt("duration")));
        return film;
    };

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(film);
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        film.setId(newId.intValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        final String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ? WHERE id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration().getSeconds(), film.getId());
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        final String sql = "SELECT * FROM films WHERE id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sql, Mappers.getFilmRowMapper(), id);
            return film;
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
        return false;
    }

    @Override
    public boolean contains(Integer id) {
        return false;
    }
}
