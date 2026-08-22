package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.VehicleCategoryDTO;

import java.util.List;

public interface VehicleCategoryService {
    void saveVehicleCategory(VehicleCategoryDTO vehicleCategoryDTO);

    List<VehicleCategoryDTO> getAllVehicleCategories();

    VehicleCategoryDTO selectVehicleCategory(Long categoryId);

    void updateVehicleCategory(VehicleCategoryDTO vehicleCategoryDTO);

    void deleteVehicleCategory(Long categoryId);
}
