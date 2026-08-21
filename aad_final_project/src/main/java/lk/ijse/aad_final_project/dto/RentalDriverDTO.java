package lk.ijse.aad_final_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalDriverDTO {
    private Long rentalDriverId;
    private Long rentalId;
    private Long driverId;
}
