package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.VehicleInspectionDTO;
import lk.ijse.aad_final_project.service.VehicleInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/vehicle-inspection")
@CrossOrigin
@RequiredArgsConstructor
public class VehicleInspectionController {

    private final VehicleInspectionService vehicleInspectionService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveVehicleInspection(@RequestBody VehicleInspectionDTO vehicleInspectionDTO) {
        vehicleInspectionService.saveVehicleInspection(vehicleInspectionDTO);
        CommonResponse response = new CommonResponse(0, "Vehicle Inspection Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllVehicleInspections() {
        List<VehicleInspectionDTO> inspectionDTOList = vehicleInspectionService.getAllVehicleInspections();
        CommonResponse response = new CommonResponse(0, inspectionDTOList, "Get All Vehicle Inspections API Successful");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{inspectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectVehicleInspection(@PathVariable Long inspectionId) {
        VehicleInspectionDTO inspectionDTO = vehicleInspectionService.selectVehicleInspection(inspectionId);
        CommonResponse response = new CommonResponse(0, inspectionDTO, "Vehicle Inspection Selected Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateVehicleInspection(@RequestBody VehicleInspectionDTO vehicleInspectionDTO) {
        vehicleInspectionService.updateVehicleInspection(vehicleInspectionDTO);
        CommonResponse response = new CommonResponse(0, "Vehicle Inspection Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{inspectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteVehicleInspection(@PathVariable Long inspectionId) {
        vehicleInspectionService.deleteVehicleInspection(inspectionId);
        CommonResponse response = new CommonResponse(0, "Vehicle Inspection Deleted Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
