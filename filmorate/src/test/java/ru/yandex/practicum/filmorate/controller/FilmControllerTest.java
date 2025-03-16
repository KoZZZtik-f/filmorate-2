package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FilmControllerTest {

    @Mock
    private FilmService filmService;

    @InjectMocks
    private FilmController filmController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Film film;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Регистрируем модуль для Java 8 date/time types
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Отключаем запись дат как таймстампов

        film = new Film();
        film.setId(1);
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(Duration.ofMinutes(148));
        film.setGenres(Set.of(new Genre(1, "Action")));
        film.setDirector(new Director(1, "Thomas"));
        film.setMpa(new Mpa(1, "PG-13"));
    }

    @Test
    void createFilm_ShouldReturnCreatedFilm() throws Exception {
        when(filmService.createFilm(any(Film.class))).thenReturn(film);

        try {
            String filmJson = objectMapper.writeValueAsString(film);
            mockMvc.perform(post("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(filmJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Inception"));
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Логируем исключение
            fail("Failed to serialize film object: " + e.getMessage());
        }

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Inception"));

        verify(filmService, times(1)).createFilm(any(Film.class));
    }

    @Test
    void updateFilm_ShouldReturnUpdatedFilm() throws Exception {
        film.setDescription("Updated description");
        when(filmService.updateFilm(any(Film.class))).thenReturn(film);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated description"));

        verify(filmService, times(1)).updateFilm(any(Film.class));
    }

    @Test
    void getAllFilms_ShouldReturnAllFilms() throws Exception {
        when(filmService.getAllFilms()).thenReturn(List.of(film));

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Inception"));

        verify(filmService, times(1)).getAllFilms();
    }

    @Test
    void getFilmById_ShouldReturnCorrectFilm() throws Exception {
        when(filmService.getFilmById(1)).thenReturn(film);

        mockMvc.perform(get("/films/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(filmService, times(1)).getFilmById(1);
    }

    @Test
    void addLike_ShouldCallServiceMethod() throws Exception {
        doNothing().when(filmService).addLike(1, 100);

        mockMvc.perform(put("/films/1/like/100"))
                .andExpect(status().isOk());

        verify(filmService, times(1)).addLike(1, 100);
    }

    @Test
    void removeLike_ShouldCallServiceMethod() throws Exception {
        doNothing().when(filmService).removeLike(1, 100);

        mockMvc.perform(delete("/films/1/like/100"))
                .andExpect(status().isOk());

        verify(filmService, times(1)).removeLike(1, 100);
    }

    @Test
    void getMostPopularFilms_ShouldReturnPopularFilms() throws Exception {
        when(filmService.getMostPopularFilms(5)).thenReturn(List.of(film));

        mockMvc.perform(get("/films/popular?count=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Inception"));

        verify(filmService, times(1)).getMostPopularFilms(5);
    }

    @Test
    void getMostPopularFilms_ShouldUseDefaultCount() throws Exception {
        when(filmService.getMostPopularFilms(10)).thenReturn(List.of(film));

        mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk());

        verify(filmService, times(1)).getMostPopularFilms(10);
    }

    @Test
    void handleValidationExceptions_ShouldReturnBadRequest() throws Exception {
        Film invalidFilm = new Film();
        invalidFilm.setName(" "); // Invalid name

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidFilm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void handleNotFoundException_ShouldReturnNotFound() throws Exception {
        when(filmService.getFilmById(999)).thenThrow(new NotFoundException("Film not found"));

        mockMvc.perform(get("/films/999"))
                .andExpect(status().isNotFound());
    }
}