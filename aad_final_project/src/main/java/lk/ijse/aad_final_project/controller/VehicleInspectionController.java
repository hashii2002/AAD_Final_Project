package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.VehicleInspectionDTO;
import lk.ijse.aad_final_project.service.VehicleInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/vehicle-inspection")
@CrossOrigin
@RequiredArgsConstructor
public class VehicleInspectionController {

    private final VehicleInspectionService vehicleInspectionService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveVehicleInspection(@RequestBody VehicleInspectionDTO vehicleInspectionDTO) {
        vehicleInspectionService.saveVehicleInspection(vehicleInspectionDTO);
        return new CommonResponse(0, "Vehicle Inspection Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllVehicleInspections() {
        List<VehicleInspectionDTO> inspectionDTOList = vehicleInspectionService.getAllVehicleInspections();
        return new CommonResponse(0, inspectionDTOList, "Get All Vehicle Inspections API Successful");
    }

    @GetMapping(value = "/select/{inspectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectVehicleInspection(@PathVariable Long inspectionId) {
        VehicleInspectionDTO inspectionDTO = vehicleInspectionService.selectVehicleInspection(inspectionId);
        return new CommonResponse(0, inspectionDTO, "Vehicle Inspection Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateVehicleInspection(@RequestBody VehicleInspectionDTO vehicleInspectionDTO) {
        vehicleInspectionService.updateVehicleInspection(vehicleInspectionDTO);
        return new CommonResponse(0, "Vehicle Inspection Updated Successfully");
    }

    @DeleteMapping(value = "/{inspectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteVehicleInspection(@PathVariable Long inspectionId) {
        vehicleInspectionService.deleteVehicleInspection(inspectionId);
        return new CommonResponse(0, "Vehicle Inspection Deleted Successfully");
    }
}
