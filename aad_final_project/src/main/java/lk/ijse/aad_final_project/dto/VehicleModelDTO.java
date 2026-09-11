package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.ijse.aad_final_project.enums.FuelType;
import lk.ijse.aad_final_project.enums.TransmissionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class VehicleModelDTO {
    private Long modelId;

    @NotNull(message = "Brand ID is required")
    private Long brandId;

    @NotBlank(message = "Model name is required")
    private String modelName;

    @NotNull(message = "Fuel type is required")
    private FuelType fuelType;

    @NotNull(message = "Seating capacity is required")
    @Min(value = 1, message = "Seating capacity must be at least 1")
    private Integer seatingCapacity;

    @NotNull(message = "Transmission type is required")
    private TransmissionType transmissionType;
}
