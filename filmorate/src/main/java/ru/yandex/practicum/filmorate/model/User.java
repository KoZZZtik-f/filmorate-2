package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class User {

    private Integer id;

    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Login cannot be blank")
    private String login;

    //If empty -> name = login (In Service)
    private String name;

    @PastOrPresent(message = "Birthday must be in past")
    private LocalDate birthday;

    Set<Integer> friends;

}
