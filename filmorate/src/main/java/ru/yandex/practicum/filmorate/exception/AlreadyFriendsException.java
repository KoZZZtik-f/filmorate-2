package ru.yandex.practicum.filmorate.exception;

public class AlreadyFriendsException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Пользователи уже являются друзьями";

    public AlreadyFriendsException() {
        super(DEFAULT_MESSAGE);
    }

    public AlreadyFriendsException(String message) {
        super(message);
    }
}
