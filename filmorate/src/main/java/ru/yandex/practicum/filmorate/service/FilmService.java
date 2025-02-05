package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FilmService {

    private static final String NOT_FOUND_MESSAGE = "Film с id %d не найден";
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final UserService userService;
    private final LikeStorage likeStorage;


    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(Integer filmId, Integer userId) {
        likeStorage.addLike(filmId, userId);
    }

    public void removeLike(Integer id, Integer filmId) {
        userService.checkUserNotFound(id);

        Film film = filmStorage.getFilmById(id);
        film.getLikes().remove(filmId);
        log.info("User {} remove like fiml {} ({}). Likes = {}", filmId, film.getId(), film.getName(), film.getLikes().size());
    }

    public Collection<Film> getMostPopularFilms(Integer count) {
        var allFilms = filmStorage.getAllFilms();

        Comparator<Film> likesComparator = new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o2.getLikes().size() - o1.getLikes().size();
            }
        };

        return allFilms.stream()
                .sorted(likesComparator)
                .limit(count)
                .toList();
    }


    public void checkFilmNotFound(Integer...  ids) {
        for (Integer id : ids) {
            if (!filmStorage.contains(id)) {
                throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, id));
            }
        }
    }
    public void checkFilmNotFound(Film... films) {
        for (Film film : films) {
            checkFilmNotFound(film.getId());
        }
    }


}
