package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void removeFilmById(int id) {
    }

    @Override
    public Collection<Film> getAllFilms() {
        return map.values();
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        return List.of();
    }

//    @Override
//    public Collection<Film> getMostPopularFilms(Integer count) {
//
//
//    }

    @Override
    public boolean contains(Film user) {
        return map.containsKey(user.getId());
    }

    @Override
    public boolean contains(Integer id) {
        return map.containsKey(id);
    }
}
