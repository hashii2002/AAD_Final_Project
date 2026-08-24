package lk.ijse.aad_final_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReviewDTO {
    private Long reviewId;
    private Integer rating;
    private String comment;
    private Long rentalId;
    private Long customerId;
}
