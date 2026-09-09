package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.VehicleCategoryDTO;
import lk.ijse.aad_final_project.entity.VehicleCategory;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.repository.VehicleCategoryRepository;
import lk.ijse.aad_final_project.service.VehicleCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleCategoryServiceImpl implements VehicleCategoryService {

    private final VehicleCategoryRepository vehicleCategoryRepository;

    @Override
    public void saveVehicleCategory(VehicleCategoryDTO vehicleCategoryDTO) {
        VehicleCategory vehicleCategory = new VehicleCategory();

        vehicleCategory.setCategory(vehicleCategoryDTO.getCategory());
        vehicleCategory.setDescription(vehicleCategoryDTO.getDescription());

        vehicleCategoryRepository.save(vehicleCategory);

    }

    @Override
    public List<VehicleCategoryDTO> getAllVehicleCategories() {
        List<VehicleCategory> vehicleCategories = vehicleCategoryRepository.findAll();

        List<VehicleCategoryDTO> vehicleCategoryDTOList = new ArrayList<>();

        for (VehicleCategory vehicleCategory : vehicleCategories) {

            VehicleCategoryDTO vehicleCategoryDTO = new VehicleCategoryDTO();

            vehicleCategoryDTO.setCategoryId(vehicleCategory.getCategoryId());
            vehicleCategoryDTO.setCategory(vehicleCategory.getCategory());
            vehicleCategoryDTO.setDescription(vehicleCategory.getDescription());

            vehicleCategoryDTOList.add(vehicleCategoryDTO);
        }

        return vehicleCategoryDTOList;
    }

    @Override
    public VehicleCategoryDTO selectVehicleCategory(Long categoryId) {
        Optional<VehicleCategory> optionalVehicleCategory = vehicleCategoryRepository.findById(categoryId);
        if (optionalVehicleCategory.isEmpty()) {
            throw new NotFoundException("Vehicle category not found");
        }

        VehicleCategory vehicleCategory = optionalVehicleCategory.get();

        VehicleCategoryDTO vehicleCategoryDTO = new VehicleCategoryDTO();

        vehicleCategoryDTO.setCategoryId(vehicleCategory.getCategoryId());
        vehicleCategoryDTO.setCategory(vehicleCategory.getCategory());
        vehicleCategoryDTO.setDescription(vehicleCategory.getDescription());

        return vehicleCategoryDTO;
    }

    @Override
    public void updateVehicleCategory(VehicleCategoryDTO vehicleCategoryDTO) {

        Optional<VehicleCategory> optionalVehicleCategory = vehicleCategoryRepository.findById(vehicleCategoryDTO.getCategoryId());
        if (optionalVehicleCategory.isEmpty()) {
            throw new NotFoundException("Vehicle category not found");
        }

        VehicleCategory vehicleCategory = optionalVehicleCategory.get();

        vehicleCategory.setCategory(vehicleCategoryDTO.getCategory());
        vehicleCategory.setDescription(vehicleCategoryDTO.getDescription());

        vehicleCategoryRepository.save(vehicleCategory);
    }

    @Override
    public void deleteVehicleCategory(Long categoryId) {
        if (!vehicleCategoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Vehicle category not found");
        }

        vehicleCategoryRepository.deleteById(categoryId);

    }
}
