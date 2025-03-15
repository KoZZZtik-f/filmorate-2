package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.serializer.DurationSerializer;
import ru.yandex.practicum.filmorate.validator.ReleaseDateValidation;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    @NotNull(message = "ID cannot be null")
    private int id;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Description cannot be null")
    @Size(max = 200, message = "Description must be less than 200 characters")
    private String description;

    @NotNull(message = "Release date cannot be null")
    @ReleaseDateValidation(message = "Release date must be on or after December 28, 1895")
    private LocalDate releaseDate;

    @NotNull(message = "Duration cannot be null")
    @DurationMin(seconds = 1, message = "Duration must be at least 1 second")
    @JsonSerialize(using = DurationSerializer.class)
    private Duration duration;

    @JsonIgnore
    private Set<Genre> genres;

    @JsonIgnore
    private Set<Integer> likes;

    private Director director;

    private Mpa mpa;

    public Film() {
        likes = new HashSet<>();
    }

}
