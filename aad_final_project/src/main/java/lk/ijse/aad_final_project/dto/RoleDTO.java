package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.NotNull;
import lk.ijse.aad_final_project.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RoleDTO {

    private Long roleId;

    @NotNull(message = "Role name is required")
    private RoleName roleName;
}
