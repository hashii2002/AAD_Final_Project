package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.ReviewDTO;
import lk.ijse.aad_final_project.entity.Customer;
import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.entity.Review;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.repository.CustomerRepository;
import lk.ijse.aad_final_project.repository.RentalRepository;
import lk.ijse.aad_final_project.repository.ReviewRepository;
import lk.ijse.aad_final_project.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        if (reviewDTO.getRating() == null || reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        if (reviewDTO.getRentalId() == null) {
            throw new RuntimeException("Rental ID is required");
        }

        Optional<Customer> optionalCustomer = customerRepository.findCustomerByUsername(username);
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("Customer not found");
        }

        Customer customer = optionalCustomer.get();

        Optional<Rental> optionalRental = rentalRepository.findById(reviewDTO.getRentalId());
        if (optionalRental.isEmpty()) {
            throw new NotFoundException("Rental not found");
        }

        Rental rental = optionalRental.get();

        if (!rental.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new RuntimeException("You can only review your own rental");
        }

        Optional<Review> existingReview = reviewRepository.findByRental_RentalId(rental.getRentalId());
        if (existingReview.isPresent()) {
            throw new RuntimeException("This rental already has a review");
        }

        if (rental.getStatus() != null && !rental.getStatus().name().equals("COMPLETED")) {
            throw new RuntimeException("You can review the rental only after it is completed");
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

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isEmpty()) {
            throw new NotFoundException("Review not found");
        }
        return mapToDTO(optionalReview.get());
    }

    @Override
    @Transactional
    public void updateReview(ReviewDTO reviewDTO, String username) {

        if (reviewDTO.getReviewId() == null) {
            throw new RuntimeException("Review ID is required");
        }

        Optional<Review> optionalReview = reviewRepository.findById(reviewDTO.getReviewId());
        if (optionalReview.isEmpty()) {
            throw new NotFoundException("Review not found");
        }

        Review review = optionalReview.get();

        Optional<Customer> optionalCustomer = customerRepository.findCustomerByUsername(username);

        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("Customer not found");
        }

        Customer customer = optionalCustomer.get();

        if (!review.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new RuntimeException("You can only update your own review");
        }

        if (reviewDTO.getRating() == null || reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, String username, String role) {

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isEmpty()) {
            throw new NotFoundException("Review not found");
        }

        Review review = optionalReview.get();

        if (role.equals("ADMIN")) {
            reviewRepository.deleteById(reviewId);
            return;
        }

        Optional<Customer> optionalCustomer = customerRepository.findCustomerByUsername(username);
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("Customer not found");
        }

        Customer customer = optionalCustomer.get();

        if (!review.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new RuntimeException("You can only delete your own review");
        }
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public List<ReviewDTO> getMyReviews(String username) {

        List<Review> reviews = reviewRepository.findByCustomer_User_Username(username);
        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        for (Review review : reviews) {
            reviewDTOList.add(mapToDTO(review));
        }

        return reviewDTOList;
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
