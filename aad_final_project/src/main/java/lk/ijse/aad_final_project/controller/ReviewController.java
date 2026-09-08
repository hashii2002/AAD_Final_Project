package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.ReviewDTO;
import lk.ijse.aad_final_project.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    public CommonResponse saveReview(@RequestBody ReviewDTO reviewDTO, Authentication authentication) {
        String username = authentication.getName();
        reviewService.saveReview(reviewDTO, username);
        return new CommonResponse(0, "Review Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllReviews() {
        List<ReviewDTO> reviewDTOList = reviewService.getAllReviews();
        return new CommonResponse(0, reviewDTOList, "Get All Reviews API Successful");
    }

    @GetMapping(value = "/select/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectReview(@PathVariable Long reviewId) {
        ReviewDTO reviewDTO = reviewService.selectReview(reviewId);
        return new CommonResponse(0, reviewDTO, "Review Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateReview(@RequestBody ReviewDTO reviewDTO, Authentication authentication) {
        String username = authentication.getName();
        reviewService.updateReview(reviewDTO, username);
        return new CommonResponse(0, "Review Updated Successfully");
    }

    @DeleteMapping(value = "/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteReview(@PathVariable Long reviewId, Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream().findFirst().get().getAuthority().replace("ROLE_", "");
        reviewService.deleteReview(reviewId, username, role);

        return new CommonResponse(0, "Review Deleted Successfully"
        );
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getMyReviews(Authentication authentication) {
        String username = authentication.getName();
        List<ReviewDTO> reviewDTOList = reviewService.getMyReviews(username);
        return new CommonResponse(0, reviewDTOList, "My Reviews Retrieved Successfully");
    }
}