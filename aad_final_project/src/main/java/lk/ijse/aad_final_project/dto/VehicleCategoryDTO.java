package lk.ijse.aad_final_project.dto;

import lk.ijse.aad_final_project.enums.Categoryname;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class VehicleCategoryDTO {
    private Long categoryId;

    private Categoryname category;

    private String description;
}
