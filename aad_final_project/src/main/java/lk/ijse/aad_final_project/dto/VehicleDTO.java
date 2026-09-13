package lk.ijse.aad_final_project.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.ijse.aad_final_project.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {

    private Long vehicleId;

    @NotNull(message = "Model ID is required")
    private Long modelId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "Vehicle color cannot be empty")
    private String color;

    @NotBlank(message = "Vehicle number is required")
    private String vehicleNo;

    @NotNull(message = "Vehicle status is required")
    private VehicleStatus status;

    @NotNull(message = "Vehicle year is required")
    private Integer year;
}
