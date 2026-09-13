package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.RoleDTO;
import lk.ijse.aad_final_project.entity.Role;
import lk.ijse.aad_final_project.exception.NotFoundException;
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
        Optional<Role> optionalRole =
                roleRepository.findById(roleDTO.getRoleId());

        if (optionalRole.isEmpty()) {
            throw new NotFoundException("Role not found");
        }

        Role role = optionalRole.get();

        role.setRoleName(roleDTO.getRoleName());

        roleRepository.save(role);

    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {

        if (!roleRepository.existsById(roleId)) {
            throw new NotFoundException("Role not found");
        }

        roleRepository.deleteById(roleId);

    }
}
