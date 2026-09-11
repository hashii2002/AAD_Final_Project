package lk.ijse.aad_final_project.controller;

import jakarta.validation.Valid;
import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.ReviewDTO;
import lk.ijse.aad_final_project.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/review")
@CrossOrigin
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveReview(@Valid @RequestBody ReviewDTO reviewDTO, Authentication authentication) {
        String username = authentication.getName();
        reviewService.saveReview(reviewDTO, username);
        CommonResponse response = new CommonResponse(0, "Review Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllReviews() {
        List<ReviewDTO> reviewDTOList = reviewService.getAllReviews();
        CommonResponse response = new CommonResponse(0, reviewDTOList, "Get All Reviews API Successful");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectReview(@PathVariable Long reviewId) {
        ReviewDTO reviewDTO = reviewService.selectReview(reviewId);
        CommonResponse response = new CommonResponse(0, reviewDTO, "Review Selected Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateReview(@Valid @RequestBody ReviewDTO reviewDTO, Authentication authentication) {
        String username = authentication.getName();
        reviewService.updateReview(reviewDTO, username);
        CommonResponse response = new CommonResponse(0, "Review Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteReview(@PathVariable Long reviewId, Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream().findFirst().get().getAuthority().replace("ROLE_", "");
        reviewService.deleteReview(reviewId, username, role);
        CommonResponse response = new CommonResponse(0, "Review Deleted Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getMyReviews(Authentication authentication) {
        String username = authentication.getName();
        List<ReviewDTO> reviewDTOList = reviewService.getMyReviews(username);
        CommonResponse response = new CommonResponse(0, reviewDTOList, "My Reviews Retrieved Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}