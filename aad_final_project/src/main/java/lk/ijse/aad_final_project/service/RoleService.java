package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    void saveRole(RoleDTO roleDTO);

    List<RoleDTO> getAllRoles();

    RoleDTO selectRole(Long roleId);

    void updateRole(RoleDTO roleDTO);

    void deleteRole(Long roleId);
}
