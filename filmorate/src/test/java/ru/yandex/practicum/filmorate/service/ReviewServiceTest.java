package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewStorage reviewStorage;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;

    @BeforeEach
    void setUp() {
        review = new Review();
        review.setId(1);
        review.setFilmId(10);
        review.setUserId(100);
        review.setContent("Great movie!");
        review.setIsPositive(true);
        review.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createReview_shouldCallReviewStorage() {
        // Arrange
        when(reviewStorage.createReview(any(Review.class))).thenReturn(review);

        // Act
        Review createdReview = reviewService.createReview(review);

        // Assert
        assertEquals(review, createdReview);
        verify(reviewStorage, times(1)).createReview(review);
    }

    @Test
    void updateReview_shouldCallReviewStorage() {
        // Arrange
        when(reviewStorage.updateReview(any(Review.class))).thenReturn(review);

        // Act
        Review updatedReview = reviewService.updateReview(review);

        // Assert
        assertEquals(review, updatedReview);
        verify(reviewStorage, times(1)).updateReview(review);
    }

    @Test
    void deleteReview_shouldCallReviewStorage() {
        // Arrange
        doNothing().when(reviewStorage).deleteReview(anyInt());

        // Act
        reviewService.deleteReview(review.getId());

        // Assert
        verify(reviewStorage, times(1)).deleteReview(review.getId());
    }

    @Test
    void getReviewById_shouldReturnReview_whenExists() {
        // Arrange
        when(reviewStorage.getReviewById(review.getId())).thenReturn(review);

        // Act
        Review foundReview = reviewService.getReviewById(review.getId());

        // Assert
        assertEquals(review, foundReview);
        verify(reviewStorage, times(1)).getReviewById(review.getId());
    }

    @Test
    void getReviewsForFilm_shouldReturnReviewList() {
        // Arrange
        Collection<Review> reviews = Arrays.asList(review);
        when(reviewStorage.getReviewsForFilm(review.getFilmId())).thenReturn(reviews);

        // Act
        Collection<Review> result = reviewService.getReviewsForFilm(review.getFilmId());

        // Assert
        assertEquals(reviews, result);
        verify(reviewStorage, times(1)).getReviewsForFilm(review.getFilmId());
    }
}
