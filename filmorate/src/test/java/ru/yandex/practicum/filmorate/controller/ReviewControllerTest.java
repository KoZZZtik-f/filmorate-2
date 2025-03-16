package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Review review;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Для поддержки LocalDateTime

        review = new Review();
        review.setId(1);
        review.setFilmId(1);
        review.setUserId(1);
        review.setContent("Great movie!");
        review.setIsPositive(true);
        review.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createReview_shouldReturnCreatedReview() throws Exception {
        Mockito.when(reviewService.createReview(any(Review.class))).thenReturn(review);

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(review.getId()))
                .andExpect(jsonPath("$.filmId").value(review.getFilmId()))
                .andExpect(jsonPath("$.userId").value(review.getUserId()))
                .andExpect(jsonPath("$.content").value(review.getContent()))
                .andExpect(jsonPath("$.isPositive").value(review.getIsPositive()));

        Mockito.verify(reviewService, Mockito.times(1)).createReview(any(Review.class));
    }

    @Test
    void updateReview_shouldReturnUpdatedReview() throws Exception {
        review.setContent("Updated review content");
        Mockito.when(reviewService.updateReview(any(Review.class))).thenReturn(review);

        mockMvc.perform(put("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated review content"));

        Mockito.verify(reviewService, Mockito.times(1)).updateReview(any(Review.class));
    }

    @Test
    void deleteReview_shouldCallDeleteMethod() throws Exception {
        Mockito.doNothing().when(reviewService).deleteReview(anyInt());

        mockMvc.perform(delete("/reviews/{id}", 1))
                .andExpect(status().isOk());

        Mockito.verify(reviewService, Mockito.times(1)).deleteReview(1);
    }

    @Test
    void getReviewById_shouldReturnReview() throws Exception {
        Mockito.when(reviewService.getReviewById(anyInt())).thenReturn(review);

        mockMvc.perform(get("/reviews/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(review.getId()))
                .andExpect(jsonPath("$.filmId").value(review.getFilmId()))
                .andExpect(jsonPath("$.userId").value(review.getUserId()))
                .andExpect(jsonPath("$.content").value(review.getContent()))
                .andExpect(jsonPath("$.isPositive").value(review.getIsPositive()));

        Mockito.verify(reviewService, Mockito.times(1)).getReviewById(1);
    }

    @Test
    void getReviewsForFilm_shouldReturnReviewsForFilm() throws Exception {
        Review review2 = new Review();
        review2.setId(2);
        review2.setFilmId(1);
        review2.setUserId(2);
        review2.setContent("Another great movie!");
        review2.setIsPositive(true);
        review2.setCreatedAt(LocalDateTime.now());

        Mockito.when(reviewService.getReviewsForFilm(anyInt())).thenReturn(List.of(review, review2));

        mockMvc.perform(get("/reviews/film/{filmId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(review.getId()))
                .andExpect(jsonPath("$[0].filmId").value(review.getFilmId()))
                .andExpect(jsonPath("$[0].userId").value(review.getUserId()))
                .andExpect(jsonPath("$[0].content").value(review.getContent()))
                .andExpect(jsonPath("$[0].isPositive").value(review.getIsPositive()))
                .andExpect(jsonPath("$[1].id").value(review2.getId()))
                .andExpect(jsonPath("$[1].filmId").value(review2.getFilmId()))
                .andExpect(jsonPath("$[1].userId").value(review2.getUserId()))
                .andExpect(jsonPath("$[1].content").value(review2.getContent()))
                .andExpect(jsonPath("$[1].isPositive").value(review2.getIsPositive()));

        Mockito.verify(reviewService, Mockito.times(1)).getReviewsForFilm(1);
    }
}