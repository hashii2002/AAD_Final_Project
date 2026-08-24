package lk.ijse.aad_final_project.dto;

import lk.ijse.aad_final_project.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {

    private Long userId;

    private Long roleId;

    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String phone;

    private UserStatus status;

    private Long customerId;

    private Long driverId;
}
