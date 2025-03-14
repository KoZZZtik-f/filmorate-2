package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import jakarta.validation.Valid;
import java.util.Collection;

@Service
@Validated
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;

    public Review createReview(@Valid Review review) {
        return reviewStorage.createReview(review);
    }

    public Review updateReview(@Valid Review review) {
        return reviewStorage.updateReview(review);
    }

    public void deleteReview(int id) {
        reviewStorage.deleteReview(id);
    }

    public Review getReviewById(int id) {
        return reviewStorage.getReviewById(id);
    }

    public Collection<Review> getReviewsForFilm(int filmId) {
        return reviewStorage.getReviewsForFilm(filmId);
    }
}
