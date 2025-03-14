package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Review {

    private Integer id;

    @NotNull(message = "Film ID cannot be null")
    private Integer filmId;

    @NotNull(message = "User ID cannot be null")
    private Integer userId;

    @NotBlank(message = "Review text cannot be blank")
    private String content;

    @NotNull(message = "isPositive cannot be null")
    private Boolean isPositive;

    private LocalDateTime createdAt = LocalDateTime.now();
}
