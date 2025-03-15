package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.Getter;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

public class Mappers {

    @Getter
    private static final RowMapper<User> userRowMapper = (resultSet, rowNum) -> {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return user;
    };

    @Getter
    private static final RowMapper<Film> filmRowMapper = (resultSet, rowNum) -> {
        Film film = new Film();
        film.setId(resultSet.getInt("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(Duration.ofSeconds(resultSet.getLong("duration")));

        // Маппинг режиссёра
        int directorId = resultSet.getInt("director_id");
        if (!resultSet.wasNull()) {
            Director director = new Director();
            director.setId(directorId);
            director.setName(resultSet.getString("director_name"));
            film.setDirector(director);
        }

        // Маппинг MPA
        int mpaId = resultSet.getInt("mpa_id");
        if (!resultSet.wasNull()) {
            Mpa mpa = new Mpa();
            mpa.setId(mpaId);
            mpa.setName(resultSet.getString("mpa_name"));
            film.setMpa(mpa);
        }

        return film;
    };

    @Getter
    private static final RowMapper<Genre> genreRowMapper = (resultSet, rowNum) -> {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    };

}
