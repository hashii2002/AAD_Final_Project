package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.ijse.aad_final_project.enums.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class VehicleBrandDTO {
    private Long brandId;

    @NotBlank(message = "Brand name is required")
    private String brandName;

    @NotNull(message = "Country is required")
    private Country country;
}
