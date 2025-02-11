package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class User {

    private int id;

    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Login cannot be blank")
    private String login;

    //If empty -> name = login (In Service)
    private String name;

    @PastOrPresent(message = "Birthday must be in past")
    private LocalDate birthday;




}
