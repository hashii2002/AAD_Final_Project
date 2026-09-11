package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lk.ijse.aad_final_project.enums.InspectionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleInspectionDTO {

    private Long inspectionId;

    @NotNull(message = "Inspection type is required")
    private InspectionType inspectionType;

    private LocalDateTime inspectionDate;

    @NotNull(message = "Fuel level is required")
    private String fuelLevel;

    @NotNull(message = "Mileage is required")
    @DecimalMin(value = "0.0", message = "Mileage cannot be negative")
    private Double mileage;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotNull(message = "Rental ID is required")
    private Long rentalId;

    @NotNull(message = "Inspected by ID is required")
    private Long inspectedById;
}