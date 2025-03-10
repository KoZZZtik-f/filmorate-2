package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(int userId) {
        super(String.format("User with id %d not found", userId));
    }

}
