package ru.yandex.practicum.filmorate.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.AlreadyFriendsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@RestControllerAdvice(assignableTypes = {FilmController.class, UserController.class})
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(final RuntimeException e) {
        return new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        return new ErrorResponse(NotFoundException.class.getSimpleName(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyFriendsException(final AlreadyFriendsException e) {
        return new ErrorResponse(AlreadyFriendsException.class.getSimpleName(), e.getMessage());
    }



}
