package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.ReviewDTO;
import lk.ijse.aad_final_project.entity.Customer;
import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.entity.Review;
import lk.ijse.aad_final_project.enums.RentalStatus;
import lk.ijse.aad_final_project.exception.DuplicateException;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
import lk.ijse.aad_final_project.repository.CustomerRepository;
import lk.ijse.aad_final_project.repository.RentalRepository;
import lk.ijse.aad_final_project.repository.ReviewRepository;
import lk.ijse.aad_final_project.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public void saveReview(ReviewDTO reviewDTO, String username) {
        validateReviewDTO(reviewDTO);

        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }

        Customer customer = customerRepository.findCustomerByUsername(username.trim())
                .orElseThrow(() -> new NotFoundException("Customer not found for username: " + username));

        Rental rental = rentalRepository.findById(reviewDTO.getRentalId())
                .orElseThrow(() -> new NotFoundException("Rental record not found"));

        if (!rental.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new ValidationException("You can only review your own rental");
        }

        if (!RentalStatus.COMPLETED.equals(rental.getStatus())) {
            throw new ValidationException("You can review the rental only after it is completed");
        }

        if (reviewRepository.existsByRental_RentalId(rental.getRentalId())) {
            throw new DuplicateException("This rental already has an associated review");
        }

        Review review = new Review();
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setRental(rental);
        review.setCustomer(customer);

        reviewRepository.save(review);
    }

    @Override
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        for (Review review : reviews) {
            reviewDTOList.add(mapToDTO(review));
        }
        return reviewDTOList;
    }

    @Override
    public ReviewDTO selectReview(Long reviewId) {
        if (reviewId == null) {
            throw new ValidationException("Review ID is required");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with ID: " + reviewId));

        return mapToDTO(review);
    }

    @Override
    @Transactional
    public void updateReview(ReviewDTO reviewDTO, String username) {
        if (reviewDTO == null || reviewDTO.getReviewId() == null) {
            throw new ValidationException("Review ID is required for update");
        }

        validateReviewDTO(reviewDTO);

        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }

        Review review = reviewRepository.findById(reviewDTO.getReviewId())
                .orElseThrow(() -> new NotFoundException("Review not found"));

        Customer customer = customerRepository.findCustomerByUsername(username.trim())
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        if (!review.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new ValidationException("You can only update your own review");
        }

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, String username, String role) {
        if (reviewId == null) {
            throw new ValidationException("Review ID is required");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        if ("ADMIN".equalsIgnoreCase(role)) {
            reviewRepository.delete(review);
            return;
        }

        Customer customer = customerRepository.findCustomerByUsername(username)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        if (!review.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new ValidationException("You can only delete your own review");
        }

        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewDTO> getMyReviews(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }

        List<Review> reviews = reviewRepository.findByCustomer_User_Username(username.trim());
        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        for (Review review : reviews) {
            reviewDTOList.add(mapToDTO(review));
        }

        return reviewDTOList;
    }

    private void validateReviewDTO(ReviewDTO dto) {
        if (dto == null) {
            throw new ValidationException("Review data is required");
        }
        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new ValidationException("Rating must be between 1 and 5");
        }
        if (dto.getRentalId() == null) {
            throw new ValidationException("Rental ID is required");
        }
    }

    private ReviewDTO mapToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(review.getReviewId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());

        if (review.getRental() != null) {
            dto.setRentalId(review.getRental().getRentalId());
        }
        if (review.getCustomer() != null) {
            dto.setCustomerId(review.getCustomer().getCustomerId());
        }
        return dto;
    }
}