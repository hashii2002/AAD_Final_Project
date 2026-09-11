package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lk.ijse.aad_final_project.enums.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DriverDTO {
    private Long driverId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "License number is required")
    @Size(max = 30, message = "License number cannot exceed 30 characters")
    private String licenseNo;

    @NotNull(message = "Driver status is required")
    private DriverStatus status;
}
