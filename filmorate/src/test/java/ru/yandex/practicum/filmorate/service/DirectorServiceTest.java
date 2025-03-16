package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectorServiceTest {

    @Mock
    private DirectorStorage directorStorage;

    @InjectMocks
    private DirectorService directorService;

    private Director director1;
    private Director director2;

    @BeforeEach
    void beforeEach() {
        director1 = new Director(1, "Christopher Nolan");
        director2 = new Director(2, "Lana Wachowski");
    }

    @Test
    void createDirector_shouldCallDirectorStorage() {
        // Arrange
        when(directorStorage.createDirector(director1)).thenReturn(director1);

        // Act
        Director createdDirector = directorService.createDirector(director1);

        // Assert
        assertEquals(director1, createdDirector);
        verify(directorStorage, times(1)).createDirector(director1);
    }

    @Test
    void updateDirector_shouldCallDirectorStorage() {
        // Arrange
        when(directorStorage.updateDirector(director1)).thenReturn(director1);

        // Act
        Director updatedDirector = directorService.updateDirector(director1);

        // Assert
        assertEquals(director1, updatedDirector);
        verify(directorStorage, times(1)).updateDirector(director1);
    }

    @Test
    void getDirectorById_shouldReturnDirector_whenExists() {
        // Arrange
        when(directorStorage.getDirectorById(1)).thenReturn(director1);

        // Act
        Director director = directorService.getDirectorById(1);

        // Assert
        assertEquals(director1, director);
        verify(directorStorage, times(1)).getDirectorById(1);
    }

    @Test
    void getDirectorById_shouldReturnNull_whenNotExists() {
        // Arrange
        when(directorStorage.getDirectorById(999)).thenReturn(null);

        // Act
        Director director = directorService.getDirectorById(999);

        // Assert
        assertNull(director);
        verify(directorStorage, times(1)).getDirectorById(999);
    }

    @Test
    void getAllDirectors_shouldReturnDirectors() {
        // Arrange
        Collection<Director> expectedDirectors = Arrays.asList(director1, director2);
        when(directorStorage.getAllDirectors()).thenReturn(expectedDirectors);

        // Act
        Collection<Director> directors = directorService.getAllDirectors();

        // Assert
        assertEquals(expectedDirectors, directors);
        verify(directorStorage, times(1)).getAllDirectors();
    }

    @Test
    void deleteDirector_shouldCallDirectorStorage() {
        // Arrange
        doNothing().when(directorStorage).deleteDirector(1);

        // Act
        directorService.deleteDirector(1);

        // Assert
        verify(directorStorage, times(1)).deleteDirector(1);
    }
}
