package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreStorage genreStorage;

    @InjectMocks
    private GenreService genreService;

    private Genre genre1;
    private Genre genre2;

    @BeforeEach
    void beforeEach() {
        genre1 = new Genre(1, "Action");
        genre2 = new Genre(2, "Sci-Fi");
    }

    @Test
    void getAllGenres_shouldReturnGenres() {
        // Arrange
        Collection<Genre> expectedGenres = Arrays.asList(genre1, genre2);
        when(genreStorage.getAllGenres()).thenReturn(expectedGenres);

        // Act
        Collection<Genre> returnedGenres = genreService.getAllGenres();

        // Assert
        Assertions.assertEquals(returnedGenres, expectedGenres);
        Mockito.verify(genreStorage, times(1)).getAllGenres();
    }

    @Test
    void getGenreById_shouldReturnGenre_whenExists() {
        // Arrange
        when(genreStorage.getGenreById(1)).thenReturn(genre1);

        // Act
        Genre genre = genreService.getGenreById(1);

        // Assert
        assertEquals(genre1, genre);
        verify(genreStorage, times(1)).getGenreById(1);
    }

    @Test
    void getGenreById_shouldReturnNull_whenNotExists() {
        // Arrange
        when(genreStorage.getGenreById(999)).thenReturn(null);

        // Act
        Genre genre = genreService.getGenreById(999);

        // Assert
        assertEquals(null, genre);
        verify(genreStorage, times(1)).getGenreById(999);
    }
}
