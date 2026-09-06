package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.VehicleCategoryDTO;
import lk.ijse.aad_final_project.service.VehicleCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/category")
@RequiredArgsConstructor
@CrossOrigin
public class VehiclecategoryController {

    private final VehicleCategoryService vehicleCategoryService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveVehicleCategory(@RequestBody VehicleCategoryDTO vehicleCategoryDTO) {
        vehicleCategoryService.saveVehicleCategory(vehicleCategoryDTO);
        return new CommonResponse(0, "Vehicle Category Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllVehicleCategories() {
        List<VehicleCategoryDTO> allCategories = vehicleCategoryService.getAllVehicleCategories();
        return new CommonResponse(0, allCategories, "Get All Vehicle Categories API Successful");
    }

    @GetMapping(value = "/select/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectVehicleCategory(@PathVariable Long categoryId) {
        VehicleCategoryDTO vehicleCategoryDTO = vehicleCategoryService.selectVehicleCategory(categoryId);
        return new CommonResponse(0, vehicleCategoryDTO, "Vehicle Category Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateVehicleCategory(@RequestBody VehicleCategoryDTO vehicleCategoryDTO) {
        vehicleCategoryService.updateVehicleCategory(vehicleCategoryDTO);
        return new CommonResponse(0, "Vehicle Category Updated Successfully");
    }

    @DeleteMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteVehicleCategory(@PathVariable Long categoryId) {
        vehicleCategoryService.deleteVehicleCategory(categoryId);
        return new CommonResponse(0, "Vehicle Category Deleted Successfully");
    }
}
