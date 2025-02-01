package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> map = new HashMap<>();


    @Override
    public Film createFilm(Film film) {
        if (map.containsKey(film.getId())) {
            throw new RuntimeException();
        }
        map.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        return map.put(film.getId(), film);
    }

    @Override
    public Film getFilmById(int id) {
        return map.get(id);
    }

    @Override
    public Film removeFilmById(int id) {
        return map.remove(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return map.values();
    }

    @Override
    public Collection<Film> getMostPopularFilms(Integer count) {
        Comparator<Film> likesComparator = new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o1.getLikes().size() - o2.getLikes().size();
            }
        };

        return map.values().stream()
                .sorted(likesComparator)
                .limit(count)
                .toList();

    }
}
