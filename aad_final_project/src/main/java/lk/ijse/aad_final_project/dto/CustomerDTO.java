package lk.ijse.aad_final_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {

    private Long customerId;

    private Long userId;

    private String nic;

    private String address;

    private String drivingLicenseNumber;
}
