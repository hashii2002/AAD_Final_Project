package lk.ijse.aad_final_project.dto;

import lk.ijse.aad_final_project.enums.DriverStatus;
import lk.ijse.aad_final_project.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverProfileDTO {

    private Long driverId;

    private Long userId;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private String licenseNo;

    private DriverStatus driverStatus;

    private UserStatus userStatus;
}