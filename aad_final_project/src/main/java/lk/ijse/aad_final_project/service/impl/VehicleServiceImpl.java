package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.VehicleDTO;
import lk.ijse.aad_final_project.entity.Vehicle;
import lk.ijse.aad_final_project.entity.VehicleCategory;
import lk.ijse.aad_final_project.entity.VehicleModel;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.repository.VehicleCategoryRepository;
import lk.ijse.aad_final_project.repository.VehicleModelRepository;
import lk.ijse.aad_final_project.repository.VehicleRepository;
import lk.ijse.aad_final_project.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleCategoryRepository vehicleCategoryRepository;

    @Override
    @Transactional
    public void saveVehicle(VehicleDTO vehicleDTO) {Optional<VehicleModel> optionalModel = vehicleModelRepository.findById(vehicleDTO.getModelId());

        if (optionalModel.isEmpty()) {
            throw new NotFoundException("Vehicle model not found");
        }

        Optional<VehicleCategory> optionalCategory = vehicleCategoryRepository.findById(vehicleDTO.getCategoryId());
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException("Vehicle category not found");
        }

        VehicleModel vehicleModel = optionalModel.get();
        VehicleCategory vehicleCategory = optionalCategory.get();

        Vehicle vehicle = new Vehicle();

        vehicle.setColor(vehicleDTO.getColor());
        vehicle.setVehicleNo(vehicleDTO.getVehicleNo());
        vehicle.setStatus(vehicleDTO.getStatus());
        vehicle.setYear(vehicleDTO.getYear());
        vehicle.setModel(vehicleModel);
        vehicle.setCategory(vehicleCategory);

        vehicleRepository.save(vehicle);
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        List<VehicleDTO> vehicleDTOList = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {

            VehicleDTO vehicleDTO = new VehicleDTO();

            vehicleDTO.setVehicleId(vehicle.getVehicleId());
            vehicleDTO.setModelId(vehicle.getModel().getModelId());
            vehicleDTO.setCategoryId(vehicle.getCategory().getCategoryId());
            vehicleDTO.setColor(vehicle.getColor());
            vehicleDTO.setVehicleNo(vehicle.getVehicleNo());
            vehicleDTO.setStatus(vehicle.getStatus());
            vehicleDTO.setYear(vehicle.getYear());

            vehicleDTOList.add(vehicleDTO);
        }

        return vehicleDTOList;
    }

    @Override
    public VehicleDTO selectVehicle(Long vehicleId) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
        if (optionalVehicle.isEmpty()) {
            throw new NotFoundException("Vehicle not found");
        }

        Vehicle vehicle = optionalVehicle.get();

        VehicleDTO vehicleDTO = new VehicleDTO();

        vehicleDTO.setVehicleId(vehicle.getVehicleId());
        vehicleDTO.setModelId(vehicle.getModel().getModelId());
        vehicleDTO.setCategoryId(vehicle.getCategory().getCategoryId());
        vehicleDTO.setColor(vehicle.getColor());
        vehicleDTO.setVehicleNo(vehicle.getVehicleNo());
        vehicleDTO.setStatus(vehicle.getStatus());
        vehicleDTO.setYear(vehicle.getYear());

        return vehicleDTO;
    }

    @Override
    @Transactional
    public void updateVehicle(VehicleDTO vehicleDTO) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleDTO.getVehicleId());
        if (optionalVehicle.isEmpty()) {
            throw new NotFoundException("Vehicle not found");
        }

        Optional<VehicleModel> optionalModel = vehicleModelRepository.findById(vehicleDTO.getModelId());
        if (optionalModel.isEmpty()) {
            throw new NotFoundException("Vehicle model not found");
        }

        Optional<VehicleCategory> optionalCategory = vehicleCategoryRepository.findById(vehicleDTO.getCategoryId());
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException("Vehicle category not found");
        }

        Vehicle vehicle = optionalVehicle.get();

        VehicleModel vehicleModel = optionalModel.get();
        VehicleCategory vehicleCategory = optionalCategory.get();

        vehicle.setColor(vehicleDTO.getColor());
        vehicle.setVehicleNo(vehicleDTO.getVehicleNo());
        vehicle.setStatus(vehicleDTO.getStatus());
        vehicle.setYear(vehicleDTO.getYear());
        vehicle.setModel(vehicleModel);
        vehicle.setCategory(vehicleCategory);

        vehicleRepository.save(vehicle);

    }

    @Override
    @Transactional
    public void deleteVehicle(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new NotFoundException("Vehicle not found");
        }
        vehicleRepository.deleteById(vehicleId);

    }
}
