package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {

    void saveReview(ReviewDTO reviewDTO,String username);

    List<ReviewDTO> getAllReviews();

    ReviewDTO selectReview(Long reviewId);

    void updateReview(ReviewDTO reviewDTO, String username);

    void deleteReview(Long reviewId,String username,String role);

    List<ReviewDTO> getMyReviews(String username);
}
