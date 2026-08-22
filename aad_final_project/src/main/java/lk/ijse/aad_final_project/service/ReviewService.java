package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {

    void saveReview(ReviewDTO reviewDTO);

    List<ReviewDTO> getAllReviews();

    ReviewDTO selectReview(Long reviewId);

    void updateReview(ReviewDTO reviewDTO);

    void deleteReview(Long reviewId);
}
