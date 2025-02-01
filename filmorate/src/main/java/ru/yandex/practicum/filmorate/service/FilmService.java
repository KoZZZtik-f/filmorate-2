package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;


    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(Integer id, Integer userId) {
        log.info("Film debug {}", getFilmById(id) );
        Film film = filmStorage.getFilmById(id);
        film.getLikes().add(userId);
        log.info("User {} liked fiml {} ({}). Likes = {}", userId, film.getId(), film.getName(), film.getLikes().size());
    }

    public void removeLike(Integer id, Integer userId) {
        Film film = filmStorage.getFilmById(id);
        film.getLikes().remove(userId);
        log.info("User {} remove like fiml {} ({}). Likes = {}", userId, film.getId(), film.getName(), film.getLikes().size());
    }

    public Collection<Film> getMostPopularFilms(Integer count) {
        return filmStorage.getMostPopularFilms(count);
    }

}
