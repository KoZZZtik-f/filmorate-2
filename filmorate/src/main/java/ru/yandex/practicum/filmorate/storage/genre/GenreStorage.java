package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {
    Collection<Genre> getAllGenres();

    Genre getGenreById(Integer id);

    public Collection<Genre> getGenresByFilmId(Integer filmId);
}
