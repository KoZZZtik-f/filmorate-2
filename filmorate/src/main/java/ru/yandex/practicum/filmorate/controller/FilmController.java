package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    //TODO Добавление фильма
    @PostMapping
    private Film createFilm(@Valid @RequestBody Film film) {
        log.info("Create film {}", film.getName());
        return filmService.createFilm(film);
    }

    //TODO Обновление фильма
    @PutMapping
    private Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Update film {}", film.getName());
        return filmService.updateFilm(film);
    }
    //TODO Получение всех фильмов
    @GetMapping
    private Collection<Film> getAllFilms() {
        return filmService.getAllFilms(); //empty
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer id) {
        return filmService.getFilmById(id);
    }
    
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        log.info("Received request to add like for film {} by user {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getMostPopularFilms(@RequestParam(required = false) Integer count) {
        count = count == null ? 10 : count;
        if (count < 0) {
            throw new RuntimeException();
        }
        return filmService.getMostPopularFilms(count);
    }


}
