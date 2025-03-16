package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GenreControllerTest {

    @Mock
    private GenreService genreService;

    @InjectMocks
    private GenreController genreController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Genre genre;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(genreController).build();
        objectMapper = new ObjectMapper();

        genre = new Genre();
        genre.setId(1);
        genre.setName("Genre Name");
    }

    @Test
    void getGenreById_shouldReturnGenre() throws Exception {
        Mockito.when(genreService.getGenreById(any(Integer.class))).thenReturn(genre);

        mockMvc.perform(get("/genres/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(genre.getId()))
                .andExpect(jsonPath("$.name").value(genre.getName()));

        Mockito.verify(genreService, Mockito.times(1)).getGenreById(1);
    }

    @Test
    void getAllGenres_shouldReturnAllGenres() throws Exception {
        Genre genre2 = new Genre();
        genre2.setId(2);
        genre2.setName("Another Genre");

        Mockito.when(genreService.getAllGenres()).thenReturn(List.of(genre, genre2));

        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(genre.getId()))
                .andExpect(jsonPath("$[0].name").value(genre.getName()))
                .andExpect(jsonPath("$[1].id").value(genre2.getId()))
                .andExpect(jsonPath("$[1].name").value(genre2.getName()));

        Mockito.verify(genreService, Mockito.times(1)).getAllGenres();
    }
}