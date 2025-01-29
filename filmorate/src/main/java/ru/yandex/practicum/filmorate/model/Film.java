package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {

    private int id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Size(max = 200, message = "Description must be less, than 200 characters")
    private String description;

    @ReleaseDate(message = "Date must be after 28 december 1895")
    private LocalDate releaseDate;

    @Positive
    private Duration duration;
}
