package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.ReviewDTO;
import lk.ijse.aad_final_project.entity.Customer;
import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.entity.Review;
import lk.ijse.aad_final_project.repository.CustomerRepository;
import lk.ijse.aad_final_project.repository.RentalRepository;
import lk.ijse.aad_final_project.repository.ReviewRepository;
import lk.ijse.aad_final_project.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void saveReview(ReviewDTO reviewDTO) {
        Optional<Rental> optionalRental = rentalRepository.findById(reviewDTO.getRentalId());
        if (optionalRental.isEmpty()) {
            throw new RuntimeException("Rental not found");
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(reviewDTO.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Rental rental = optionalRental.get();
        Customer customer = optionalCustomer.get();

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
            ReviewDTO dto = new ReviewDTO();
            dto.setReviewId(review.getReviewId());
            dto.setRating(review.getRating());
            dto.setComment(review.getComment());
            dto.setRentalId(review.getRental().getRentalId());
            dto.setCustomerId(review.getCustomer().getCustomerId());

            reviewDTOList.add(dto);
        }

        return reviewDTOList;
    }

    @Override
    public ReviewDTO selectReview(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isEmpty()) {
            throw new RuntimeException("Review not found");
        }

        Review review = optionalReview.get();

        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(review.getReviewId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setRentalId(review.getRental().getRentalId());
        dto.setCustomerId(review.getCustomer().getCustomerId());

        return dto;
    }

    @Override
    public void updateReview(ReviewDTO reviewDTO) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewDTO.getReviewId());

        if (optionalReview.isEmpty()) {
            throw new RuntimeException("Review not found");
        }

        Review review = optionalReview.get();
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new RuntimeException("Review not found");
        }

        reviewRepository.deleteById(reviewId);
    }
}
