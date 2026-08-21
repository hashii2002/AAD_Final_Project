package lk.ijse.aad_final_project.dto;

import lk.ijse.aad_final_project.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDTO {
    private Long vehicleId;

    private Long modelId;

    private Long categoryId;

    private String color;

    private String vehicleNo;

    private VehicleStatus status;

    private Integer year;
}
