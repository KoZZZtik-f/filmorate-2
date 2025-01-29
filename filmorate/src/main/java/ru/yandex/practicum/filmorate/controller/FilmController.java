package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    //TODO Добавление фильма
    @PostMapping
    private Film createFilm(@RequestBody Film film) {
        log.info("Create film {}", film.getName());
        return new Film();
    }

    //TODO Обновление фильма
    @PutMapping
    private Film updateFilm(@RequestBody Film film) {
        log.info("Update film {}", film.getName());
        return new Film();
    }
    //TODO Получение всех фильмов
    @GetMapping
    private List<Film> getAllFilms() {

        return new ArrayList<>(); //empty
    }


}
