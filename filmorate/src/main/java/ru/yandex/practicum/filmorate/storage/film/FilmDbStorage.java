package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.mapper.Mappers;

import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("release_date", film.getReleaseDate());
        parameters.put("duration", film.getDuration().toSeconds());
        parameters.put("mpa_id", film.getMpa() != null ? film.getMpa().getId() : null);

        if (film.getDirector() != null) {
            parameters.put("director_id", film.getDirector().getId());
        }

        int filmId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
        film.setId(filmId);

        saveGenres(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        final String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, " +
                "director_id = ?, mpa_id = ? WHERE id = ?";

        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration().toSeconds(),
                film.getDirector() != null ? film.getDirector().getId() : null,
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId());

        updateGenres(film);
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        final String sql = "SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
                "d.id AS director_id, d.name AS director_name, " +
                "m.id AS mpa_id, m.name AS mpa_name " +
                "FROM films f " +
                "LEFT JOIN directors d ON f.director_id = d.id " +
                "LEFT JOIN mpas m ON f.mpa_id = m.id " +
                "WHERE f.id = ?";

        try {
            Film film = jdbcTemplate.queryForObject(sql, Mappers.getFilmRowMapper(), id);
            film.setGenres(getGenresForFilm(id));
            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(id);
        }
    }

    private void saveGenres(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String sql = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sql, film.getId(), genre.getId());
            }
        }
    }

    private void updateGenres(Film film) {
        String deleteSql = "DELETE FROM films_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteSql, film.getId());
        saveGenres(film);
    }

    private Set<Genre> getGenresForFilm(int filmId) {
        String sql = "SELECT g.id, g.name FROM genres g " +
                "JOIN films_genres fg ON g.id = fg.genre_id " +
                "WHERE fg.film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, Mappers.getGenreRowMapper(), filmId));
    }

    @Override
    public void removeFilmById(int id) {
        final String sql = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        final String sql = "SELECT f.*, m.id AS mpa_id, m.name AS mpa_name FROM films f " +
                "LEFT JOIN mpas m ON f.mpa_id = m.id";
        return jdbcTemplate.query(sql, Mappers.getFilmRowMapper());
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        final String sql = """
                    SELECT f.id, f.name, f.description, f.release_date, f.duration, 
                           f.mpa_id, m.name AS mpa_name, d.id AS director_id, d.name AS director_name,
                           COUNT(fl.user_id) AS likes_count
                    FROM films f
                    LEFT JOIN likes fl ON f.id = fl.film_id
                    LEFT JOIN mpas m ON f.mpa_id = m.id
                    LEFT JOIN directors d ON f.director_id = d.id
                    GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.name, d.id, d.name
                    ORDER BY likes_count DESC
                    LIMIT ?
                """;
        return jdbcTemplate.query(sql, Mappers.getFilmRowMapper(), count);
    }

    @Override
    public boolean contains(Film film) {
        return contains(film.getId());
    }

    @Override
    public boolean contains(Integer id) {
        final String sql = "SELECT EXISTS(SELECT 1 FROM films WHERE id = ?);";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

}
