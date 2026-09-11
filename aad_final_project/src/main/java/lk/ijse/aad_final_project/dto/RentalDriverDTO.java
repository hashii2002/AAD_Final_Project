package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RentalDriverDTO {
    private Long rentalDriverId;

    @NotNull(message = "Rental ID is required")
    private Long rentalId;

    @NotNull(message = "Driver ID is required")
    private Long driverId;
    private String driverName;
    private String driverPhone;
    private String driverLicenseNo;
}
