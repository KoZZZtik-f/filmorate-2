package ru.yandex.practicum.filmorate.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;

@RestControllerAdvice(assignableTypes = {FilmController.class, UserController.class})
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(final RuntimeException e) {
        return new ErrorResponse("error", "description");
    }



}
