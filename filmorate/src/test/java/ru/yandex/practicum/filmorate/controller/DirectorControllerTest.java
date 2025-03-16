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
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DirectorControllerTest {

    @Mock
    private DirectorService directorService;

    @InjectMocks
    private DirectorController directorController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Director director;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(directorController).build();
        objectMapper = new ObjectMapper();

        director = new Director();
        director.setId(1);
        director.setName("Director Name");
    }

    @Test
    void getAllDirectors_shouldReturnAllDirectors() throws Exception {
        Director director2 = new Director();
        director2.setId(2);
        director2.setName("Another Director");

        Mockito.when(directorService.getAllDirectors()).thenReturn(List.of(director, director2));

        mockMvc.perform(get("/directors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(director.getId()))
                .andExpect(jsonPath("$[0].name").value(director.getName()))
                .andExpect(jsonPath("$[1].id").value(director2.getId()))
                .andExpect(jsonPath("$[1].name").value(director2.getName()));

        Mockito.verify(directorService, Mockito.times(1)).getAllDirectors();
    }

    @Test
    void getDirectorById_shouldReturnDirector() throws Exception {
        Mockito.when(directorService.getDirectorById(anyInt())).thenReturn(director);

        mockMvc.perform(get("/directors/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(director.getId()))
                .andExpect(jsonPath("$.name").value(director.getName()));

        Mockito.verify(directorService, Mockito.times(1)).getDirectorById(1);
    }

    @Test
    void createDirector_shouldReturnCreatedDirector() throws Exception {
        Mockito.when(directorService.createDirector(any(Director.class))).thenReturn(director);

        mockMvc.perform(post("/directors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(director)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(director.getId()))
                .andExpect(jsonPath("$.name").value(director.getName()));

        Mockito.verify(directorService, Mockito.times(1)).createDirector(any(Director.class));
    }

    @Test
    void updateDirector_shouldReturnUpdatedDirector() throws Exception {
        director.setName("Updated Director Name");
        Mockito.when(directorService.updateDirector(any(Director.class))).thenReturn(director);

        mockMvc.perform(put("/directors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(director)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(director.getId()))
                .andExpect(jsonPath("$.name").value("Updated Director Name"));

        Mockito.verify(directorService, Mockito.times(1)).updateDirector(any(Director.class));
    }

    @Test
    void deleteDirector_shouldCallDeleteMethod() throws Exception {
        Mockito.doNothing().when(directorService).deleteDirector(anyInt());

        mockMvc.perform(delete("/directors/{id}", 1))
                .andExpect(status().isOk());

        Mockito.verify(directorService, Mockito.times(1)).deleteDirector(1);
    }
}