package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

public class FilmNotFoundException extends NotFoundException {
    public FilmNotFoundException(int filmId) {
        super(String.format("Film with id %d not found", filmId));
    }
}
