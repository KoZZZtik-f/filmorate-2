package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @Mock
    private FilmStorage filmStorage;
    @Mock
    private UserStorage userStorage;
    @Mock
    private UserService userService;
    @Mock
    private LikeStorage likeStorage;

    @InjectMocks
    private FilmService filmService;

    private Film film1; // has 1 like
    private Film film2; // has 2 likes
    private Film film3; // has 3 likes


    @BeforeEach
    void beforeEach() {
        film1 = new Film();
        film1.setId(1);
        film1.setName("Inception");
        film1.setDescription("Sci-Fi Thriller");
        film1.setReleaseDate(LocalDate.of(2010, 7, 16));
        film1.setDuration(Duration.ofMinutes(148));
        film1.setLikes(Set.of(1));
        film1.setMpa(new Mpa(5, "PG-13"));
        film1.setGenres(Set.of(new Genre(1, "Action"), new Genre(2, "Sci-Fi")));
        film1.setDirector(new Director(1, "Christopher Nolan"));

        film2 = new Film();
        film2.setId(2);
        film2.setName("The Matrix");
        film2.setDescription("Cyberpunk Action");
        film2.setReleaseDate(LocalDate.of(1999, 3, 31));
        film2.setDuration(Duration.ofMinutes(136));
        film2.setLikes(Set.of(2, 3));
        film2.setMpa(new Mpa(4, "R"));
        film2.setGenres(Set.of(new Genre(2, "Sci-Fi"), new Genre(3, "Thriller")));
        film2.setDirector(new Director(2, "Lana Wachowski"));

        film3 = new Film();
        film3.setId(3);
        film3.setName("Interstellar");
        film3.setDescription("Epic Space Adventure");
        film3.setReleaseDate(LocalDate.of(2014, 11, 7));
        film3.setDuration(Duration.ofMinutes(169));
        film3.setLikes(Set.of(1, 4, 5));
        film3.setMpa(new Mpa(5, "PG-13"));
        film3.setGenres(Set.of(new Genre(1, "Action"), new Genre(2, "Sci-Fi"), new Genre(4, "Drama")));
        film3.setDirector(new Director(1, "Christopher Nolan"));
    }


    @Test
    void createFilm_shouldCallFilmStorage() {
        Mockito.when(filmStorage.createFilm(any(Film.class))).thenReturn(film1);

        Film createdFilm = filmService.createFilm(film1);

        assertEquals(film1, createdFilm);
        verify(filmStorage, times(1)).createFilm(film1);
    }

    @Test
    void getFilmById_shouldReturnFilm() {
        Mockito.when(filmStorage.getFilmById(1)).thenReturn(film1);

        Film result = filmService.getFilmById(1);

        assertEquals(film1, result);
        verify(filmStorage, times(1)).getFilmById(1);
    }

    @Test
    void addLike_shouldCallLikeStorage() {
        filmService.addLike(1, 2);

        verify(likeStorage, times(1)).addLike(1, 2);
    }

    @Test
    void removeLike_shouldCallLikeStorage() {
        filmService.removeLike(1, 2);

        verify(likeStorage, times(1)).removeLike(1, 2);
    }

    @Test
    void getMostPopularFilms_shouldReturnFilms() {
        Mockito.when(filmStorage.getMostPopularFilms(3)).thenReturn(List.of(film1));

        List<Film> popularFilms = filmService.getMostPopularFilms(3);

        assertEquals(3, popularFilms.size());
        assertTrue(popularFilms.contains(film1));

        verify(filmStorage, times(1)).getMostPopularFilms(3);
    }
}

