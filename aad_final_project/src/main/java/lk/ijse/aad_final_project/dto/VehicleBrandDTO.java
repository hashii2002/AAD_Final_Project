package lk.ijse.aad_final_project.dto;

import lk.ijse.aad_final_project.enums.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class VehicleBrandDTO {
    private Long brandId;

    private String brandName;

    private Country country;
}
