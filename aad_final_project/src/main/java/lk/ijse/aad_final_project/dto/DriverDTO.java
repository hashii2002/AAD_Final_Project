package lk.ijse.aad_final_project.dto;

import lk.ijse.aad_final_project.enums.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DriverDTO {
    private Long driverId;

    private Long userId;

    private String licenseNo;

    private DriverStatus status;
}
