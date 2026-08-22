package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.VehicleInspectionDTO;

import java.util.List;

public interface VehicleInspectionService {
    void saveVehicleInspection(VehicleInspectionDTO vehicleInspectionDTO);

    List<VehicleInspectionDTO> getAllVehicleInspections();

    VehicleInspectionDTO selectVehicleInspection(Long inspectionId);

    void updateVehicleInspection(VehicleInspectionDTO vehicleInspectionDTO);

    void deleteVehicleInspection(Long inspectionId);
}
