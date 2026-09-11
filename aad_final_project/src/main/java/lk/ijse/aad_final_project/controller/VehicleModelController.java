package lk.ijse.aad_final_project.controller;

import jakarta.validation.Valid;
import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.VehicleModelDTO;
import lk.ijse.aad_final_project.service.VehicleModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/model")
@RequiredArgsConstructor
@CrossOrigin
public class VehicleModelController {

    private final VehicleModelService vehicleModelService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveVehicleModel(@Valid @RequestBody VehicleModelDTO vehicleModelDTO) {
        vehicleModelService.saveVehicleModel(vehicleModelDTO);
        CommonResponse response = new CommonResponse(0, "Vehicle Model Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllVehicleModels() {
        List<VehicleModelDTO> allModels = vehicleModelService.getAllVehicleModels();
        CommonResponse response = new CommonResponse(0, allModels, "Get All Vehicle Models API Successful");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{modelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectVehicleModel(@PathVariable Long modelId) {
        VehicleModelDTO vehicleModelDTO = vehicleModelService.selectVehicleModel(modelId);
        CommonResponse response = new CommonResponse(0, vehicleModelDTO, "Vehicle Model Selected Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateVehicleModel(@Valid @RequestBody VehicleModelDTO vehicleModelDTO) {
        vehicleModelService.updateVehicleModel(vehicleModelDTO);
        CommonResponse response = new CommonResponse(0, "Vehicle Model Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{modelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteVehicleModel(@PathVariable Long modelId) {
        vehicleModelService.deleteVehicleModel(modelId);
        CommonResponse response = new CommonResponse(0, "Vehicle Model Deleted Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
