package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;
import java.util.Collection;

public interface ReviewStorage {
    Review createReview(Review review);
    Review updateReview(Review review);
    void deleteReview(int id);
    Review getReviewById(int id);
    Collection<Review> getReviewsForFilm(int filmId);
}
