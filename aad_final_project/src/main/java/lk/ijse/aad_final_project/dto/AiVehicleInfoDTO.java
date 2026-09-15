package lk.ijse.aad_final_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiVehicleInfoDTO {

    private Long vehicleId;
    private String vehicleNo;
    private String color;
    private Integer year;
    private String status;

    private String modelName;
    private String brandName;
    private String categoryName;

    private Double dailyRate;
    private Double monthlyRate;

}
