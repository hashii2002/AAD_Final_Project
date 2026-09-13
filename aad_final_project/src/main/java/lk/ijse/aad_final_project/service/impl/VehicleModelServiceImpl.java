package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.VehicleModelDTO;
import lk.ijse.aad_final_project.entity.VehicleBrand;
import lk.ijse.aad_final_project.entity.VehicleModel;
import lk.ijse.aad_final_project.exception.DuplicateException;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
import lk.ijse.aad_final_project.repository.VehicleBrandRepository;
import lk.ijse.aad_final_project.repository.VehicleModelRepository;
import lk.ijse.aad_final_project.service.VehicleModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleModelServiceImpl implements VehicleModelService {

    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleBrandRepository vehicleBrandRepository;

    @Override
    @Transactional
    public void saveVehicleModel(VehicleModelDTO vehicleModelDTO) {
        if (vehicleModelDTO == null) {
            throw new ValidationException("Vehicle model data is required");
        }
        if (vehicleModelDTO.getBrandId() == null) {
            throw new ValidationException("Brand ID is required");
        }
        if (vehicleModelDTO.getModelName() == null || vehicleModelDTO.getModelName().isBlank()) {
            throw new ValidationException("Model name is required");
        }
        if (vehicleModelDTO.getFuelType() == null) {
            throw new ValidationException("Fuel type is required");
        }
        if (vehicleModelDTO.getSeatingCapacity() == null || vehicleModelDTO.getSeatingCapacity() < 1) {
            throw new ValidationException("Seating capacity must be at least 1");
        }
        if (vehicleModelDTO.getTransmissionType() == null) {
            throw new ValidationException("Transmission type is required");
        }

        String modelName = vehicleModelDTO.getModelName().trim();

        if (vehicleModelRepository.existsByModelNameAndBrand_BrandId(modelName, vehicleModelDTO.getBrandId())) {
            throw new DuplicateException("Vehicle model with this name already exists for the selected brand");
        }

        Optional<VehicleBrand> optionalBrand = vehicleBrandRepository.findById(vehicleModelDTO.getBrandId());
        if (optionalBrand.isEmpty()) {
            throw new NotFoundException("Vehicle brand not found");
        }

        VehicleBrand vehicleBrand = optionalBrand.get();

        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setModelName(modelName);
        vehicleModel.setFuelType(vehicleModelDTO.getFuelType());
        vehicleModel.setSeatingCapacity(vehicleModelDTO.getSeatingCapacity());
        vehicleModel.setTransmissionType(vehicleModelDTO.getTransmissionType());
        vehicleModel.setBrand(vehicleBrand);

        vehicleModelRepository.save(vehicleModel);
    }

    @Override
    public List<VehicleModelDTO> getAllVehicleModels() {
        List<VehicleModel> vehicleModels = vehicleModelRepository.findAll();
        List<VehicleModelDTO> vehicleModelDTOList = new ArrayList<>();

        for (VehicleModel vehicleModel : vehicleModels) {
            VehicleModelDTO vehicleModelDTO = new VehicleModelDTO();

            vehicleModelDTO.setModelId(vehicleModel.getModelId());
            vehicleModelDTO.setBrandId(vehicleModel.getBrand().getBrandId());
            vehicleModelDTO.setModelName(vehicleModel.getModelName());
            vehicleModelDTO.setFuelType(vehicleModel.getFuelType());
            vehicleModelDTO.setSeatingCapacity(vehicleModel.getSeatingCapacity());
            vehicleModelDTO.setTransmissionType(vehicleModel.getTransmissionType());

            vehicleModelDTOList.add(vehicleModelDTO);
        }

        return vehicleModelDTOList;
    }

    @Override
    public VehicleModelDTO selectVehicleModel(Long modelId) {
        if (modelId == null) {
            throw new ValidationException("Model ID is required");
        }

        Optional<VehicleModel> optionalVehicleModel = vehicleModelRepository.findById(modelId);
        if (optionalVehicleModel.isEmpty()) {
            throw new NotFoundException("Vehicle model not found");
        }

        VehicleModel vehicleModel = optionalVehicleModel.get();

        VehicleModelDTO vehicleModelDTO = new VehicleModelDTO();
        vehicleModelDTO.setModelId(vehicleModel.getModelId());
        vehicleModelDTO.setBrandId(vehicleModel.getBrand().getBrandId());
        vehicleModelDTO.setModelName(vehicleModel.getModelName());
        vehicleModelDTO.setFuelType(vehicleModel.getFuelType());
        vehicleModelDTO.setSeatingCapacity(vehicleModel.getSeatingCapacity());
        vehicleModelDTO.setTransmissionType(vehicleModel.getTransmissionType());

        return vehicleModelDTO;
    }

    @Override
    @Transactional
    public void updateVehicleModel(VehicleModelDTO vehicleModelDTO) {
        if (vehicleModelDTO == null) {
            throw new ValidationException("Vehicle model data is required");
        }
        if (vehicleModelDTO.getModelId() == null) {
            throw new ValidationException("Model ID is required");
        }
        if (vehicleModelDTO.getBrandId() == null) {
            throw new ValidationException("Brand ID is required");
        }
        if (vehicleModelDTO.getModelName() == null || vehicleModelDTO.getModelName().isBlank()) {
            throw new ValidationException("Model name is required");
        }
        if (vehicleModelDTO.getFuelType() == null) {
            throw new ValidationException("Fuel type is required");
        }
        if (vehicleModelDTO.getSeatingCapacity() == null || vehicleModelDTO.getSeatingCapacity() < 1) {
            throw new ValidationException("Seating capacity must be at least 1");
        }
        if (vehicleModelDTO.getTransmissionType() == null) {
            throw new ValidationException("Transmission type is required");
        }

        Optional<VehicleModel> optionalVehicleModel = vehicleModelRepository.findById(vehicleModelDTO.getModelId());
        if (optionalVehicleModel.isEmpty()) {
            throw new NotFoundException("Vehicle model not found");
        }

        Optional<VehicleBrand> optionalBrand = vehicleBrandRepository.findById(vehicleModelDTO.getBrandId());
        if (optionalBrand.isEmpty()) {
            throw new NotFoundException("Vehicle brand not found");
        }

        String modelName = vehicleModelDTO.getModelName().trim();

        if (vehicleModelRepository.existsByModelNameAndBrand_BrandIdAndModelIdNot(
                modelName, vehicleModelDTO.getBrandId(), vehicleModelDTO.getModelId())) {
            throw new DuplicateException("Vehicle model with this name already exists for the selected brand");
        }

        VehicleModel vehicleModel = optionalVehicleModel.get();
        vehicleModel.setModelName(modelName);
        vehicleModel.setFuelType(vehicleModelDTO.getFuelType());
        vehicleModel.setSeatingCapacity(vehicleModelDTO.getSeatingCapacity());
        vehicleModel.setTransmissionType(vehicleModelDTO.getTransmissionType());
        vehicleModel.setBrand(optionalBrand.get());

        vehicleModelRepository.save(vehicleModel);
    }

    @Override
    @Transactional
    public void deleteVehicleModel(Long modelId) {
        if (modelId == null) {
            throw new ValidationException("Model ID is required");
        }

        Optional<VehicleModel> optionalVehicleModel = vehicleModelRepository.findById(modelId);
        if (optionalVehicleModel.isEmpty()) {
            throw new NotFoundException("Vehicle model not found");
        }

        vehicleModelRepository.deleteById(modelId);
    }
}