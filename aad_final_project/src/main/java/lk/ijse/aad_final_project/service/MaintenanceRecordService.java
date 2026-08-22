package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.MaintenanceRecordDTO;

import java.util.List;

public interface MaintenanceRecordService {
    void saveMaintenanceRecord(MaintenanceRecordDTO maintenanceRecordDTO);

    List<MaintenanceRecordDTO> getAllMaintenanceRecords();

    MaintenanceRecordDTO selectMaintenanceRecord(Long maintenanceId);

    void updateMaintenanceRecord(MaintenanceRecordDTO maintenanceRecordDTO);

    void deleteMaintenanceRecord(Long maintenanceId);
}
