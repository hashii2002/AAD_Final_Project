package lk.ijse.aad_final_project.dto;

import lk.ijse.aad_final_project.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RoleDTO {

    private Long roleId;

    private RoleName roleName;
}
