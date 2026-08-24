package lk.ijse.aad_final_project.dto;

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
    private MaintenanceType maintenanceType;
    private String description;
    private LocalDate serviceDate;
    private LocalDate nextServiceDate;
    private Double cost;
    private MaintenanceStatus status;
    private Double mileageAtService;
    private Long vehicleId;
}
