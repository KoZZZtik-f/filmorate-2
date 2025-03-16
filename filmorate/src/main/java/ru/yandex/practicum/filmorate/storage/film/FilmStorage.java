package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(int id);

    void removeFilmById(int id);

    Collection<Film> getAllFilms();

    List<Film> getMostPopularFilms(Integer count);

    boolean contains(Film film);

    boolean contains(Integer id);
}
