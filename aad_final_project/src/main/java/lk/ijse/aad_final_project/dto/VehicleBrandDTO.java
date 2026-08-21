package lk.ijse.aad_final_project.dto;

import lk.ijse.aad_final_project.enums.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleBrandDTO {
    private Long brandId;

    private String brandName;

    private Country country;
}
