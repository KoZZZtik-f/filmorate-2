package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;
import java.util.Collection;

public interface DirectorStorage {
    Director createDirector(Director director);
    Director updateDirector(Director director);
    Director getDirectorById(Integer id);
    Collection<Director> getAllDirectors();
    void deleteDirector(Integer id);
}
