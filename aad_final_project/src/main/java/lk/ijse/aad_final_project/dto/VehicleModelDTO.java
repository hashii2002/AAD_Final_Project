package lk.ijse.aad_final_project.dto;

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

    private Long brandId;

    private String modelName;

    private FuelType fuelType;

    private Integer seatingCapacity;

    private TransmissionType transmissionType;
}
