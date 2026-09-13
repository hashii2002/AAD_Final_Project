package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.VehicleCategoryDTO;
import lk.ijse.aad_final_project.entity.VehicleCategory;
import lk.ijse.aad_final_project.exception.DuplicateException;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
import lk.ijse.aad_final_project.repository.VehicleCategoryRepository;
import lk.ijse.aad_final_project.service.VehicleCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleCategoryServiceImpl implements VehicleCategoryService {

    private final VehicleCategoryRepository vehicleCategoryRepository;

    @Override
    @Transactional
    public void saveVehicleCategory(VehicleCategoryDTO vehicleCategoryDTO) {
        if (vehicleCategoryDTO == null) {
            throw new ValidationException("Vehicle category data is required");
        }
        if (vehicleCategoryDTO.getCategory() == null) {
            throw new ValidationException("Category is required");
        }

        String description = vehicleCategoryDTO.getDescription() != null ? vehicleCategoryDTO.getDescription().trim() : null;

        if (vehicleCategoryRepository.existsByCategory(vehicleCategoryDTO.getCategory())) {
            throw new DuplicateException("Vehicle category already exists");
        }

        VehicleCategory vehicleCategory = new VehicleCategory();

        vehicleCategory.setCategory(vehicleCategoryDTO.getCategory());
        vehicleCategory.setDescription(description);

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
        if (categoryId == null) {
            throw new ValidationException("Category ID is required");
        }

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
    @Transactional
    public void updateVehicleCategory(VehicleCategoryDTO vehicleCategoryDTO) {
        if (vehicleCategoryDTO == null) {
            throw new ValidationException("Vehicle category data is required");
        }
        if (vehicleCategoryDTO.getCategoryId() == null) {
            throw new ValidationException("Category ID is required");
        }
        if (vehicleCategoryDTO.getCategory() == null) {
            throw new ValidationException("Category is required");
        }

        Optional<VehicleCategory> optionalVehicleCategory = vehicleCategoryRepository.findById(vehicleCategoryDTO.getCategoryId());
        if (optionalVehicleCategory.isEmpty()) {
            throw new NotFoundException("Vehicle category not found");
        }

        String description = vehicleCategoryDTO.getDescription() != null ? vehicleCategoryDTO.getDescription().trim() : null;

        if (vehicleCategoryRepository.existsByCategoryAndCategoryIdNot(vehicleCategoryDTO.getCategory(), vehicleCategoryDTO.getCategoryId())) {
            throw new DuplicateException("Vehicle category already exists");
        }

        VehicleCategory vehicleCategory = optionalVehicleCategory.get();

        vehicleCategory.setCategory(vehicleCategoryDTO.getCategory());
        vehicleCategory.setDescription(description);

        vehicleCategoryRepository.save(vehicleCategory);
    }

    @Override
    @Transactional
    public void deleteVehicleCategory(Long categoryId) {
        if (categoryId == null) {
            throw new ValidationException("Category ID is required");
        }

        Optional<VehicleCategory> optionalVehicleCategory = vehicleCategoryRepository.findById(categoryId);
        if (optionalVehicleCategory.isEmpty()) {
            throw new NotFoundException("Vehicle category not found");
        }

        vehicleCategoryRepository.deleteById(categoryId);
    }
}