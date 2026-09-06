package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.VehicleModelDTO;
import lk.ijse.aad_final_project.service.VehicleModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/model")
@RequiredArgsConstructor
@CrossOrigin
public class VehicleModelController {

    private final VehicleModelService vehicleModelService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveVehicleModel(@RequestBody VehicleModelDTO vehicleModelDTO) {
        vehicleModelService.saveVehicleModel(vehicleModelDTO);
        return new CommonResponse(0, "Vehicle Model Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllVehicleModels() {
        List<VehicleModelDTO> allModels = vehicleModelService.getAllVehicleModels();
        return new CommonResponse(0, allModels, "Get All Vehicle Models API Successful");
    }

    @GetMapping(value = "/select/{modelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectVehicleModel(@PathVariable Long modelId) {
        VehicleModelDTO vehicleModelDTO = vehicleModelService.selectVehicleModel(modelId);
        return new CommonResponse(0, vehicleModelDTO, "Vehicle Model Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateVehicleModel(@RequestBody VehicleModelDTO vehicleModelDTO) {
        vehicleModelService.updateVehicleModel(vehicleModelDTO);
        return new CommonResponse(0, "Vehicle Model Updated Successfully");
    }

    @DeleteMapping(value = "/{modelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteVehicleModel(@PathVariable Long modelId) {
        vehicleModelService.deleteVehicleModel(modelId);
        return new CommonResponse(0, "Vehicle Model Deleted Successfully");
    }
}
