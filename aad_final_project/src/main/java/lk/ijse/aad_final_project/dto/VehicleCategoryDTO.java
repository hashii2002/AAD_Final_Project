package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.NotNull;
import lk.ijse.aad_final_project.enums.Categoryname;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class VehicleCategoryDTO {
    private Long categoryId;

    @NotNull(message = "Category is required")
    private Categoryname category;

    private String description;
}
