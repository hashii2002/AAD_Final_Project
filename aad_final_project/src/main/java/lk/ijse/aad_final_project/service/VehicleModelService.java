package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.VehicleModelDTO;

import java.util.List;

public interface VehicleModelService {

    void saveVehicleModel(VehicleModelDTO vehicleModelDTO);

    List<VehicleModelDTO> getAllVehicleModels();

    VehicleModelDTO selectVehicleModel(Long modelId);

    void updateVehicleModel(VehicleModelDTO vehicleModelDTO);

    void deleteVehicleModel(Long modelId);
}
