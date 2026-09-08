package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.MaintenanceRecordDTO;
import lk.ijse.aad_final_project.service.MaintenanceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/maintenance")
@CrossOrigin
@RequiredArgsConstructor
public class MaintenanceRecordController {
    private final MaintenanceRecordService maintenanceRecordService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveMaintenanceRecord(@RequestBody MaintenanceRecordDTO maintenanceRecordDTO) {
        maintenanceRecordService.saveMaintenanceRecord(maintenanceRecordDTO);
        return new CommonResponse(0, "Maintenance Record Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllMaintenanceRecords() {
        List<MaintenanceRecordDTO> recordDTOList = maintenanceRecordService.getAllMaintenanceRecords();
        return new CommonResponse(0, recordDTOList, "Get All Maintenance Records API Successful");
    }

    @GetMapping(value = "/select/{maintenanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectMaintenanceRecord(@PathVariable Long maintenanceId) {
        MaintenanceRecordDTO maintenanceRecordDTO = maintenanceRecordService.selectMaintenanceRecord(maintenanceId);
        return new CommonResponse(0, maintenanceRecordDTO, "Maintenance Record Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateMaintenanceRecord(@RequestBody MaintenanceRecordDTO maintenanceRecordDTO) {
        maintenanceRecordService.updateMaintenanceRecord(maintenanceRecordDTO);
        return new CommonResponse(0, "Maintenance Record Updated Successfully");
    }

    @DeleteMapping(value = "/{maintenanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteMaintenanceRecord(@PathVariable Long maintenanceId) {
        maintenanceRecordService.deleteMaintenanceRecord(maintenanceId);
        return new CommonResponse(0, "Maintenance Record Deleted Successfully");
    }

}
