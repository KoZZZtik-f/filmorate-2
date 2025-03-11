package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private Integer id;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email address")
    private String email;

    @NotNull(message = "Login cannot be null")
    @NotBlank(message = "Login cannot be blank")
    private String login;

    // If name is null, then name = login (in service)
    private String name;

    @NotNull(message = "Birthday cannot be null")
    @PastOrPresent(message = "Birthday must be in the past or present")
    private LocalDate birthday;

    @JsonIgnore
    private Set<Integer> friends;

    public User() {
        friends = new HashSet<>();
    }
}
