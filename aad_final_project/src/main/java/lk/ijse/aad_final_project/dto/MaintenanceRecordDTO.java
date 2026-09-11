package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lk.ijse.aad_final_project.enums.MaintenanceStatus;
import lk.ijse.aad_final_project.enums.MaintenanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceRecordDTO {

    private Long maintenanceId;

    @NotNull(message = "Maintenance type is required")
    private MaintenanceType maintenanceType;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Service date is required")
    private LocalDate serviceDate;

    private LocalDate nextServiceDate;

    @NotNull(message = "Maintenance cost is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Maintenance cost cannot be negative")
    private Double cost;

    @NotNull(message = "Maintenance status is required")
    private MaintenanceStatus status;

    @NotNull(message = "Mileage at service is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Mileage cannot be negative")
    private Double mileageAtService;

    @NotNull(message = "Vehicle ID is required")
    @Positive(message = "Vehicle ID must be greater than 0")
    private Long vehicleId;
}
