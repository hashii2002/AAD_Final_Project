package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegisterDTO {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Phone Number is required")
    private String phone;

    @NotBlank(message = "NIC is required")
    @Size(min = 10, max = 20, message = "NIC must be between 10 and 20 characters")
    private String nic;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Driving license number is required")
    private String drivingLicenseNumber;
}