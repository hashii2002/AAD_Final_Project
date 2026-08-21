package lk.ijse.aad_final_project.dto;

import lk.ijse.aad_final_project.enums.InspectionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleInspectionDTO {
    private Long inspectionId;
    private InspectionType inspectionType;
    private LocalDateTime inspectionDate;
    private String fuelLevel;
    private Double mileage;
    private String notes;
    private Long vehicleId;
    private Long rentalId;
    private Long inspectedById;
}
