package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.VehicleDTO;

import java.util.List;

public interface VehicleService {
    void saveVehicle(VehicleDTO vehicleDTO);

    List<VehicleDTO> getAllVehicles();

    VehicleDTO selectVehicle(Long vehicleId);

    void updateVehicle(VehicleDTO vehicleDTO);

    void deleteVehicle(Long vehicleId);
}
