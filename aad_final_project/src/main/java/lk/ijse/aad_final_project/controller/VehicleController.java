package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.VehicleDTO;
import lk.ijse.aad_final_project.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/vehicle")
@RequiredArgsConstructor
@CrossOrigin
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveVehicle(@RequestBody VehicleDTO vehicleDTO) {
        vehicleService.saveVehicle(vehicleDTO);
        return new CommonResponse(0, "Vehicle Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllVehicles() {
        List<VehicleDTO> allVehicles = vehicleService.getAllVehicles();
        return new CommonResponse(0, allVehicles, "Get All Vehicles API Successful");
    }

    @GetMapping(value = "/select/{vehicleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectVehicle(@PathVariable Long vehicleId) {
        VehicleDTO vehicleDTO = vehicleService.selectVehicle(vehicleId);
        return new CommonResponse(0, vehicleDTO, "Vehicle Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateVehicle(@RequestBody VehicleDTO vehicleDTO) {
        vehicleService.updateVehicle(vehicleDTO);
        return new CommonResponse(0, "Vehicle Updated Successfully");
    }

    @DeleteMapping(value = "/{vehicleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteVehicle(@PathVariable Long vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return new CommonResponse(0, "Vehicle Deleted Successfully");
    }
}
