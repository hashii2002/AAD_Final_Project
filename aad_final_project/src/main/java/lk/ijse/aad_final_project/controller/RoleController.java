package lk.ijse.aad_final_project.controller;

import jakarta.validation.Valid;
import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.RoleDTO;
import lk.ijse.aad_final_project.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/role")
@CrossOrigin
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping(value = "/save-role", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveRole(@Valid @RequestBody RoleDTO roleDTO) {
        roleService.saveRole(roleDTO);
        CommonResponse response = new CommonResponse(0, "Role Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllRoles() {
        List<RoleDTO> allRoles = roleService.getAllRoles();
        CommonResponse response = new CommonResponse(0, allRoles, "Get All Roles API Successful");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectRole(@PathVariable Long roleId) {
        RoleDTO roleDTO = roleService.selectRole(roleId);
        CommonResponse response = new CommonResponse(0, roleDTO, "Role Selected Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateRole(@Valid @RequestBody RoleDTO roleDTO) {
        roleService.updateRole(roleDTO);
        CommonResponse response = new CommonResponse(0, "Role Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        CommonResponse response = new CommonResponse(0, "Role Deleted Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}