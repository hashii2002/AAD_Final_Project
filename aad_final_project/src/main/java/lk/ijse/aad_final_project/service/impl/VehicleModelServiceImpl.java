package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.VehicleModelDTO;
import lk.ijse.aad_final_project.entity.VehicleBrand;
import lk.ijse.aad_final_project.entity.VehicleModel;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.repository.VehicleBrandRepository;
import lk.ijse.aad_final_project.repository.VehicleModelRepository;
import lk.ijse.aad_final_project.service.VehicleModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleModelServiceImpl implements VehicleModelService {

    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleBrandRepository vehicleBrandRepository;

    @Override
    public void saveVehicleModel(VehicleModelDTO vehicleModelDTO) {
        Optional<VehicleBrand> optionalBrand = vehicleBrandRepository.findById(vehicleModelDTO.getBrandId());
        if (optionalBrand.isEmpty()) {
            throw new NotFoundException("Vehicle brand not found");
        }

        VehicleBrand vehicleBrand = optionalBrand.get();

        VehicleModel vehicleModel = new VehicleModel();

        vehicleModel.setModelName(vehicleModelDTO.getModelName());
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
    public void updateVehicleModel(VehicleModelDTO vehicleModelDTO) {
        Optional<VehicleModel> optionalVehicleModel = vehicleModelRepository.findById(vehicleModelDTO.getModelId());
        if (optionalVehicleModel.isEmpty()) {
            throw new NotFoundException("Vehicle model not found");
        }

        Optional<VehicleBrand> optionalBrand = vehicleBrandRepository.findById(vehicleModelDTO.getBrandId());
        if (optionalBrand.isEmpty()) {
            throw new NotFoundException("Vehicle brand not found");
        }

        VehicleModel vehicleModel = optionalVehicleModel.get();

        VehicleBrand vehicleBrand = optionalBrand.get();

        vehicleModel.setModelName(vehicleModelDTO.getModelName());
        vehicleModel.setFuelType(vehicleModelDTO.getFuelType());
        vehicleModel.setSeatingCapacity(vehicleModelDTO.getSeatingCapacity());
        vehicleModel.setTransmissionType(vehicleModelDTO.getTransmissionType());
        vehicleModel.setBrand(vehicleBrand);

        vehicleModelRepository.save(vehicleModel);

    }

    @Override
    public void deleteVehicleModel(Long modelId) {
        if (!vehicleModelRepository.existsById(modelId)) {
            throw new NotFoundException("Vehicle model not found");
        }

        vehicleModelRepository.deleteById(modelId);

    }
}
