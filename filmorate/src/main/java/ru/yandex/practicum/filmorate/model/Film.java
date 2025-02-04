package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.serializer.DurationSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private int id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Size(max = 200, message = "Description must be less, than 200 characters")
    private String description;

    private LocalDate releaseDate;

    @DurationMin(seconds = 1)
    @JsonSerialize(using = DurationSerializer.class)
    private Duration duration;

    @JsonIgnore
    private Set<Integer> likes;

    public Film() {
        likes = new HashSet<>();
    }
}
