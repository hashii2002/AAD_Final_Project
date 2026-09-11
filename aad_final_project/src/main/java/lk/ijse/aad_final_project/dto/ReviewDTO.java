package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReviewDTO {
    private Long reviewId;

    @NotNull(message = "Rating is required")
    private Integer rating;
    private String comment;

    @NotNull(message = "Rental ID is required")
    private Long rentalId;
    private Long customerId;
}
