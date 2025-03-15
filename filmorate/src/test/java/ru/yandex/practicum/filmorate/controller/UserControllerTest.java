package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc //?
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateUserSuccess() throws Exception {
        String jsonRequest = """
        {
          "login": "dolore",
          "name": "Nick Name",
          "email": "mail@mail.ru",
          "birthday": "1946-08-20"
        }
        """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"))
                .andExpect(jsonPath("$.login").value("dolore"));
    }

    @Test
    public void testCreateUserFailLogin() throws Exception {
        String jsonRequest = """
        {
          "login": "dolore ullamco",
          "email": "yandex@mail.ru",
          "birthday": "2446-08-20"
        }
        """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserFailEmail() throws Exception {
        String jsonRequest = """
        {
          "login": "dolore",
          "name": "Nick Name",
          "email": "mail.ru",
          "birthday": "1946-08-20"
        }
        """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserFailBirthday() throws Exception {
        String jsonRequest = """
        {
          "login": "dolore",
          "name": "Nick Name",
          "email": "mail@mail.ru",
          "birthday": "2446-08-20"
        }
        """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserSuccess() throws Exception {
        String jsonRequest = """
        {
          "id": 1,
          "login": "doloreUpdate",
          "name": "est adipisicing",
          "email": "mail@yandex.ru",
          "birthday": "1976-09-20"
        }
        """;

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("doloreUpdate"))
                .andExpect(jsonPath("$.name").value("est adipisicing"))
                .andExpect(jsonPath("$.email").value("mail@yandex.ru"))
                .andExpect(jsonPath("$.birthday").value("1976-09-20"));
    }

    @Test
    public void testUpdateUserUnknown() throws Exception {
        String jsonRequest = """
        {
          "id": 9999,
          "login": "doloreUpdate",
          "name": "est adipisicing",
          "email": "mail@yandex.ru",
          "birthday": "1976-09-20"
        }
        """;

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
