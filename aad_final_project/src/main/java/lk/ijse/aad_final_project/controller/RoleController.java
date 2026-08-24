package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.RoleDTO;
import lk.ijse.aad_final_project.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/role")
@CrossOrigin
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping(value = "/save-role", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveRole(@RequestBody RoleDTO roleDTO) {
        roleService.saveRole(roleDTO);

        return new CommonResponse(0, "Role Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllRoles() {
        List<RoleDTO> allRoles = roleService.getAllRoles();
        return new CommonResponse(0, allRoles, "Get All Roles API Successful");
    }

    @GetMapping(value = "/select/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectRole(@PathVariable Long roleId) {
        RoleDTO roleDTO = roleService.selectRole(roleId);
        return new CommonResponse(0, roleDTO, "Role Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateRole(@RequestBody RoleDTO roleDTO) {
        roleService.updateRole(roleDTO);
        return new CommonResponse(0, "Role Updated Successfully");
    }

    @DeleteMapping(value = "/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return new CommonResponse(0, "Role Deleted Successfully");
    }
}
