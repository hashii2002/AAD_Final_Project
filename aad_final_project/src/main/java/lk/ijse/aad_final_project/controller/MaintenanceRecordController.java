package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.MaintenanceRecordDTO;
import lk.ijse.aad_final_project.service.MaintenanceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/v1/maintenance")
@CrossOrigin
@RequiredArgsConstructor
public class MaintenanceRecordController {
    private final MaintenanceRecordService maintenanceRecordService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveMaintenanceRecord(@Valid @RequestBody MaintenanceRecordDTO maintenanceRecordDTO) {
        maintenanceRecordService.saveMaintenanceRecord(maintenanceRecordDTO);
        CommonResponse response = new CommonResponse(0, "Maintenance Record Saved Successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllMaintenanceRecords() {
        List<MaintenanceRecordDTO> recordDTOList = maintenanceRecordService.getAllMaintenanceRecords();
        CommonResponse response = new CommonResponse(0, recordDTOList, "Get All Maintenance Records API Successful");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{maintenanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectMaintenanceRecord(@PathVariable Long maintenanceId) {
        MaintenanceRecordDTO maintenanceRecordDTO = maintenanceRecordService.selectMaintenanceRecord(maintenanceId);
        CommonResponse response = new CommonResponse(0, maintenanceRecordDTO, "Maintenance Record Selected Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateMaintenanceRecord(@Valid @RequestBody MaintenanceRecordDTO maintenanceRecordDTO) {
        maintenanceRecordService.updateMaintenanceRecord(maintenanceRecordDTO);
        CommonResponse response = new CommonResponse(0, "Maintenance Record Updated Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{maintenanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteMaintenanceRecord(@PathVariable Long maintenanceId) {
        maintenanceRecordService.deleteMaintenanceRecord(maintenanceId);
        CommonResponse response = new CommonResponse(0, "Maintenance Record Deleted Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
