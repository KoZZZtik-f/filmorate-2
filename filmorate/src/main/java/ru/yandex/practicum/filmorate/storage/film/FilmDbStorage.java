package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

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
        final String sql = "insert into films (name, description, release_date, duration) values (?, ?, ?, ?)";

        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration().getSeconds());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        final String sql = "update films set name = ?, description = ?, release_date = ?, duration = ? where id = ?";

        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration().getSeconds(), film.getId());
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        final String sql = "select * from films where id = ?" ;

        return jdbcTemplate.queryForObject(sql, filmRowMapper, id);
    }

    @Override
    public void removeFilmById(int id) {
        final String sql = "delete from films where id = ?" ;

        jdbcTemplate.update(sql, id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        final String sql = "select * from films";

        List<Film> films = jdbcTemplate.query(sql, filmRowMapper);
        return films;

    }

    @Override
    public Collection<Film> getMostPopularFilms(Integer count) {
        final String sql = "select * from films";

        List<Film> films = jdbcTemplate.query(sql, filmRowMapper);
        return films;
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
