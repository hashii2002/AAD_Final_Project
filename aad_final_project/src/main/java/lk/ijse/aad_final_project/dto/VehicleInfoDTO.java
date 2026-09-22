package lk.ijse.aad_final_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleInfoDTO {

    private Long vehicleId;

    private String vehicleNo;

    private String color;

    private Integer year;

    private String brandName;

    private String modelName;

    private String categoryName;

    private String fuelType;

    private Integer seatingCapacity;

    private String transmissionType;
}
