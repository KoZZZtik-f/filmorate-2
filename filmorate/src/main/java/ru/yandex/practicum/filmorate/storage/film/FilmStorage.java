package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(int id);

    Film removeFilmById(int id);

    Collection<Film> getAllFilms();

//    Collection<Film> getMostPopularFilms(Integer count);

    boolean contains(Film film);

    boolean contains(Integer id);
}
