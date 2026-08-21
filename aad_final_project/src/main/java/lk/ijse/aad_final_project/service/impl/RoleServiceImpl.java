package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.RoleDTO;
import lk.ijse.aad_final_project.entity.Role;
import lk.ijse.aad_final_project.repository.RoleRepository;
import lk.ijse.aad_final_project.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public void saveRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());
        roleRepository.save(role);

    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleDTO> roleDTOList = new ArrayList<>();

        for (Role role : roles) {

            RoleDTO roleDTO = new RoleDTO();

            roleDTO.setRoleId(role.getRoleId());
            roleDTO.setRoleName(role.getRoleName());

            roleDTOList.add(roleDTO);
        }

        return roleDTOList;
    }

    @Override
    public RoleDTO selectRole(Long roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);

        if (optionalRole.isEmpty()) {
            throw new RuntimeException("Role not found");
        }

        Role role = optionalRole.get();

        RoleDTO roleDTO = new RoleDTO();

        roleDTO.setRoleId(role.getRoleId());
        roleDTO.setRoleName(role.getRoleName());

        return roleDTO;
    }

    @Override
    public void updateRole(RoleDTO roleDTO) {
        Optional<Role> optionalRole =
                roleRepository.findById(roleDTO.getRoleId());

        if (optionalRole.isEmpty()) {
            throw new RuntimeException("Role not found");
        }

        Role role = optionalRole.get();

        role.setRoleName(roleDTO.getRoleName());

        roleRepository.save(role);

    }

    @Override
    public void deleteRole(Long roleId) {

        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException("Role not found");
        }

        roleRepository.deleteById(roleId);

    }
}
