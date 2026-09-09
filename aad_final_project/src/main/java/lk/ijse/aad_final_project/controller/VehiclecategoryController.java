package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.VehicleCategoryDTO;
import lk.ijse.aad_final_project.service.VehicleCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/category")
@RequiredArgsConstructor
@CrossOrigin
public class VehiclecategoryController {

    private final VehicleCategoryService vehicleCategoryService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveVehicleCategory(@RequestBody VehicleCategoryDTO vehicleCategoryDTO) {
        vehicleCategoryService.saveVehicleCategory(vehicleCategoryDTO);
        CommonResponse response = new CommonResponse(0, "Vehicle Category Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllVehicleCategories() {
        List<VehicleCategoryDTO> allCategories = vehicleCategoryService.getAllVehicleCategories();
        CommonResponse response = new CommonResponse(0, allCategories, "Get All Vehicle Categories API Successful");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectVehicleCategory(@PathVariable Long categoryId) {
        VehicleCategoryDTO vehicleCategoryDTO = vehicleCategoryService.selectVehicleCategory(categoryId);
        CommonResponse response = new CommonResponse(0, vehicleCategoryDTO, "Vehicle Category Selected Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateVehicleCategory(@RequestBody VehicleCategoryDTO vehicleCategoryDTO) {
        vehicleCategoryService.updateVehicleCategory(vehicleCategoryDTO);
        CommonResponse response = new CommonResponse(0, "Vehicle Category Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteVehicleCategory(@PathVariable Long categoryId) {
        vehicleCategoryService.deleteVehicleCategory(categoryId);
        CommonResponse response = new CommonResponse(0, "Vehicle Category Deleted Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}