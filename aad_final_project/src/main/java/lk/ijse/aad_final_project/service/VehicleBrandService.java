package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.VehicleBrandDTO;

import java.util.List;

public interface VehicleBrandService {
    void saveVehicleBrand(VehicleBrandDTO vehicleBrandDTO);

    List<VehicleBrandDTO> getAllVehicleBrands();

    VehicleBrandDTO selectVehicleBrand(Long brandId);

    void updateVehicleBrand(VehicleBrandDTO vehicleBrandDTO);

    void deleteVehicleBrand(Long brandId);
}
