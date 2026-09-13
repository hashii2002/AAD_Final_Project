package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.RoleDTO;
import lk.ijse.aad_final_project.entity.Role;
import lk.ijse.aad_final_project.exception.DuplicateException;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
import lk.ijse.aad_final_project.repository.RoleRepository;
import lk.ijse.aad_final_project.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void saveRole(RoleDTO roleDTO) {
        if (roleDTO == null) {
            throw new ValidationException("Role data is required");
        }
        if (roleDTO.getRoleName() == null) {
            throw new ValidationException("Role name is required");
        }
        if (roleRepository.existsByRoleName(roleDTO.getRoleName())) {
            throw new DuplicateException("Role already exists");
        }

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
        if (roleId == null) {
            throw new ValidationException("Role ID is required");
        }

        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isEmpty()) {
            throw new NotFoundException("Role not found");
        }

        Role role = optionalRole.get();

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleId(role.getRoleId());
        roleDTO.setRoleName(role.getRoleName());

        return roleDTO;
    }

    @Override
    @Transactional
    public void updateRole(RoleDTO roleDTO) {
        if (roleDTO == null) {
            throw new ValidationException("Role data is required");
        }
        if (roleDTO.getRoleId() == null) {
            throw new ValidationException("Role ID is required");
        }
        if (roleDTO.getRoleName() == null) {
            throw new ValidationException("Role name is required");
        }

        Optional<Role> optionalRole = roleRepository.findById(roleDTO.getRoleId());
        if (optionalRole.isEmpty()) {
            throw new NotFoundException("Role not found");
        }

        if (roleRepository.existsByRoleNameAndRoleIdNot(roleDTO.getRoleName(), roleDTO.getRoleId())) {
            throw new DuplicateException("Role already exists");
        }

        Role role = optionalRole.get();
        role.setRoleName(roleDTO.getRoleName());
        roleRepository.save(role);

    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {

        if (roleId == null) {
            throw new ValidationException("Role ID is required");
        }

        if (!roleRepository.existsById(roleId)) {
            throw new NotFoundException("Role not found");
        }

        if (roleRepository.existsUsersByRoleId(roleId)) {
            throw new ValidationException(
                    "Cannot delete role because users are assigned to this role"
            );
        }

        roleRepository.deleteById(roleId);

    }
}
