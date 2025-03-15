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
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mapper.Mappers;

import java.time.Duration;
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

        //Загружаем фильм в базу
        int filmId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
        film.setId(filmId);

        // Сохраняем жанры (если они заданы)
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String sql = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sql, filmId, genre.getId());
            }
        }
        return film;
    }


    @Override
    public Film updateFilm(Film film) {
        final String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, director_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration().toSeconds(),
                (film.getDirector() != null ? film.getDirector().getId() : null),
                film.getId());

        // Обновляем жанры: удаляем старые записи и вставляем новые
        String deleteSql = "DELETE FROM films_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteSql, film.getId());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String insertSql = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(insertSql, film.getId(), genre.getId());
            }
        }
        return film;
    }



    @Override
    public Film getFilmById(int id) {
        final String sql = "SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
                "d.id AS director_id, d.name AS director_name " +
                "FROM films f " +
                "LEFT JOIN directors d ON f.director_id = d.id " +
                "LEFT JOIN mpas m ON f.mpa_id = m.id " +
                "WHERE f.id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Film f = new Film();
                f.setId(rs.getInt("id"));
                f.setName(rs.getString("name"));
                f.setDescription(rs.getString("description"));
                f.setReleaseDate(rs.getDate("release_date").toLocalDate());
                f.setDuration(Duration.ofSeconds(rs.getInt("duration")));
                // Заполняем режиссёра, если данные есть:
                int directorId = rs.getInt("director_id");
                if (directorId != 0) {
                    Director director = new Director();
                    director.setId(directorId);
                    director.setName(rs.getString("director_name"));
                    f.setDirector(director);
                }
                return f;
            }, id);

            // Подгружаем жанры фильма
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
    public Collection<Film> getMostPopularFilms(Integer count) {
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
        final String sql = "SELECT COUNT(*) FROM films WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}