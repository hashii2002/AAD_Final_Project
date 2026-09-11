package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private Long customerId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "NIC is required")
    @Size(min = 10, max = 20, message = "NIC must be between 10 and 20 characters")
    private String nic;

    @NotBlank(message = "Address is required")
    @Size(max = 300, message = "Address cannot exceed 300 characters")
    private String address;

    @NotBlank(message = "Driving license number is required")
    @Size(min = 8, max = 30, message = "Driving license number must be between 5 and 30 characters")
    private String drivingLicenseNumber;
}
