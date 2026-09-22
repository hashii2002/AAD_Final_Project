package lk.ijse.aad_final_project.dto;

import lk.ijse.aad_final_project.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProfileDTO {

    private Long customerId;
    private Long userId;

    private String username;
    private String email;

    private String firstName;
    private String lastName;
    private String phone;

    private String nic;
    private String address;
    private String drivingLicenseNumber;

    private UserStatus status;
}
