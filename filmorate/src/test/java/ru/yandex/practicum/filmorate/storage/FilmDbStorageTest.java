package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Import(FilmDbStorage.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FilmDbStorageTest {

    @Autowired
    private FilmDbStorage filmDbStorage;

    private Film film;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(Duration.ofMinutes(148));
        film.setMpa(new Mpa(1, "PG-13"));
        film.setGenres(Set.of(new Genre(1, "Action")));
    }

    @Test
    void createFilm_shouldSaveFilmToDatabase() {
        Film createdFilm = filmDbStorage.createFilm(film);

        assertNotNull(createdFilm.getId());
        assertEquals("Inception", createdFilm.getName());
        assertEquals("A mind-bending thriller", createdFilm.getDescription());
        assertEquals(LocalDate.of(2010, 7, 16), createdFilm.getReleaseDate());
        assertEquals(Duration.ofMinutes(148), createdFilm.getDuration());
        assertEquals(1, createdFilm.getMpa().getId());
        assertEquals("PG-13", createdFilm.getMpa().getName());
        assertEquals(1, createdFilm.getGenres().size());
        assertEquals("Action", createdFilm.getGenres().iterator().next().getName());
    }

    @Test
    void updateFilm_shouldUpdateFilmInDatabase() {
        Film createdFilm = filmDbStorage.createFilm(film);
        createdFilm.setName("Updated Name");
        createdFilm.setDescription("Updated Description");

        Film updatedFilm = filmDbStorage.updateFilm(createdFilm);

        assertEquals(createdFilm.getId(), updatedFilm.getId());
        assertEquals("Updated Name", updatedFilm.getName());
        assertEquals("Updated Description", updatedFilm.getDescription());
    }

    @Test
    void getFilmById_shouldReturnFilm() {
        Film createdFilm = filmDbStorage.createFilm(film);
        Film retrievedFilm = filmDbStorage.getFilmById(createdFilm.getId());

        assertNotNull(retrievedFilm);
        assertEquals(createdFilm.getId(), retrievedFilm.getId());
        assertEquals(createdFilm.getName(), retrievedFilm.getName());
    }

    @Test
    void getFilmById_shouldThrowExceptionIfFilmNotFound() {
        assertThrows(FilmNotFoundException.class, () -> filmDbStorage.getFilmById(999));
    }

    @Test
    void removeFilmById_shouldDeleteFilmFromDatabase() {
        Film createdFilm = filmDbStorage.createFilm(film);
        filmDbStorage.removeFilmById(createdFilm.getId());

        assertThrows(FilmNotFoundException.class, () -> filmDbStorage.getFilmById(createdFilm.getId()));
    }

    @Test
    void getAllFilms_shouldReturnAllFilms() {
        filmDbStorage.createFilm(film);
        Collection<Film> films = filmDbStorage.getAllFilms();

        assertFalse(films.isEmpty());
        assertTrue(films.stream().anyMatch(f -> f.getName().equals("Inception")));
    }

    @Test
    void getMostPopularFilms_shouldReturnPopularFilms() {
        filmDbStorage.createFilm(film);
        List<Film> popularFilms = filmDbStorage.getMostPopularFilms(10);

        assertFalse(popularFilms.isEmpty());
    }

    @Test
    void contains_shouldReturnTrueIfFilmExists() {
        Film createdFilm = filmDbStorage.createFilm(film);
        assertTrue(filmDbStorage.contains(createdFilm));
    }

    @Test
    void contains_shouldReturnFalseIfFilmDoesNotExist() {
        assertFalse(filmDbStorage.contains(999));
    }
}

//@SpringBootTest
//@ActiveProfiles("test")
//@Sql(scripts = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//class FilmDbStorageTest {
//
//    @Autowired
//    private FilmDbStorage filmDbStorage;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @BeforeEach
//    void setUp() {
//        // Очистка базы перед каждым тестом
//        jdbcTemplate.update("DELETE FROM films_genres");
//        jdbcTemplate.update("DELETE FROM films");
//        jdbcTemplate.update("DELETE FROM genres");
//        jdbcTemplate.update("DELETE FROM mpas");
//        jdbcTemplate.update("DELETE FROM directors");
//    }
//
//    @Test
//    void createFilm_shouldSaveFilmToDatabase() {
//        Film film = new Film();
//        film.setName("Inception");
//        film.setDescription("A mind-bending thriller");
//        film.setReleaseDate(LocalDate.of(2010, 7, 16));
//        film.setDuration(Duration.ofMinutes(148));
//        film.setMpa(new Mpa(1, "PG-13"));
//        film.setGenres(Set.of(new Genre(1, "Action")));
//
//        Film createdFilm = filmDbStorage.createFilm(film);
//
//        assertNotNull(createdFilm.getId());
//        assertEquals("Inception", createdFilm.getName());
//    }
//}