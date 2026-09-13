package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.VehicleInspectionDTO;
import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.entity.User;
import lk.ijse.aad_final_project.entity.Vehicle;
import lk.ijse.aad_final_project.entity.VehicleInspection;
import lk.ijse.aad_final_project.enums.RoleName;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.repository.RentalRepository;
import lk.ijse.aad_final_project.repository.UserRepository;
import lk.ijse.aad_final_project.repository.VehicleInspectionRepository;
import lk.ijse.aad_final_project.repository.VehicleRepository;
import lk.ijse.aad_final_project.service.VehicleInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleInspectionServiceImpl implements VehicleInspectionService {
    private final VehicleInspectionRepository vehicleInspectionRepository;
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void saveVehicleInspection(VehicleInspectionDTO vehicleInspectionDTO) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleInspectionDTO.getVehicleId());
        if (optionalVehicle.isEmpty()) {
            throw new NotFoundException("Vehicle not found");
        }

        Optional<Rental> optionalRental = rentalRepository.findById(vehicleInspectionDTO.getRentalId());
        if (optionalRental.isEmpty()) {
            throw new NotFoundException("Rental not found");
        }

        Optional<User> optionalUser = userRepository.findById(vehicleInspectionDTO.getInspectedById());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        Vehicle vehicle = optionalVehicle.get();
        Rental rental = optionalRental.get();
        User inspectedBy = optionalUser.get();

        RoleName roleName = inspectedBy.getRole().getRoleName();
        if (roleName != RoleName.ADMIN && roleName != RoleName.FLEET_MANAGER) {
            throw new RuntimeException("Only Admin or Fleet Manager can perform vehicle inspection");
        }

        if (!rental.getVehicle().getVehicleId().equals(vehicle.getVehicleId())) {
            throw new RuntimeException("Selected vehicle does not belong to this rental");
        }

        VehicleInspection vehicleInspection = new VehicleInspection();
        vehicleInspection.setInspectionType(vehicleInspectionDTO.getInspectionType());
        vehicleInspection.setInspectionDate(vehicleInspectionDTO.getInspectionDate() != null ? vehicleInspectionDTO.getInspectionDate() : LocalDateTime.now());
        vehicleInspection.setFuelLevel(vehicleInspectionDTO.getFuelLevel());
        vehicleInspection.setMileage(vehicleInspectionDTO.getMileage());
        vehicleInspection.setNotes(vehicleInspectionDTO.getNotes());
        vehicleInspection.setVehicle(vehicle);
        vehicleInspection.setRental(rental);
        vehicleInspection.setInspectedBy(inspectedBy);

        vehicleInspectionRepository.save(vehicleInspection);
    }

    @Override
    public List<VehicleInspectionDTO> getAllVehicleInspections() {
        List<VehicleInspection> inspections = vehicleInspectionRepository.findAll();
        List<VehicleInspectionDTO> inspectionDTOList = new ArrayList<>();

        for (VehicleInspection inspection : inspections) {
            VehicleInspectionDTO dto = new VehicleInspectionDTO();

            dto.setInspectionId(inspection.getInspectionId());
            dto.setInspectionType(inspection.getInspectionType());
            dto.setInspectionDate(inspection.getInspectionDate());
            dto.setFuelLevel(inspection.getFuelLevel());
            dto.setMileage(inspection.getMileage());
            dto.setNotes(inspection.getNotes());
            dto.setVehicleId(inspection.getVehicle().getVehicleId());
            dto.setRentalId(inspection.getRental().getRentalId());
            dto.setInspectedById(inspection.getInspectedBy().getUserId());

            inspectionDTOList.add(dto);
        }

        return inspectionDTOList;
    }

    @Override
    public VehicleInspectionDTO selectVehicleInspection(Long inspectionId) {
        Optional<VehicleInspection> optionalInspection = vehicleInspectionRepository.findById(inspectionId);
        if (optionalInspection.isEmpty()) {
            throw new NotFoundException("Vehicle inspection not found");
        }

        VehicleInspection inspection = optionalInspection.get();

        VehicleInspectionDTO dto = new VehicleInspectionDTO();
        dto.setInspectionId(inspection.getInspectionId());
        dto.setInspectionType(inspection.getInspectionType());
        dto.setInspectionDate(inspection.getInspectionDate());
        dto.setFuelLevel(inspection.getFuelLevel());
        dto.setMileage(inspection.getMileage());
        dto.setNotes(inspection.getNotes());
        dto.setVehicleId(inspection.getVehicle().getVehicleId());
        dto.setRentalId(inspection.getRental().getRentalId());
        dto.setInspectedById(inspection.getInspectedBy().getUserId());

        return dto;
    }

    @Override
    @Transactional
    public void updateVehicleInspection(VehicleInspectionDTO vehicleInspectionDTO) {
        Optional<VehicleInspection> optionalInspection = vehicleInspectionRepository.findById(vehicleInspectionDTO.getInspectionId());
        if (optionalInspection.isEmpty()) {
            throw new NotFoundException("Vehicle inspection not found");
        }

        VehicleInspection inspection = optionalInspection.get();

        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleInspectionDTO.getVehicleId());
        if (optionalVehicle.isEmpty()) {
            throw new NotFoundException("Vehicle not found");
        }

        Optional<Rental> optionalRental = rentalRepository.findById(vehicleInspectionDTO.getRentalId());
        if (optionalRental.isEmpty()) {
            throw new NotFoundException("Rental not found");
        }

        Optional<User> optionalUser = userRepository.findById(vehicleInspectionDTO.getInspectedById());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Inspector user not found");
        }

        Vehicle vehicle = optionalVehicle.get();
        Rental rental = optionalRental.get();
        User inspectedBy = optionalUser.get();

        RoleName roleName = inspectedBy.getRole().getRoleName();
        if (roleName != RoleName.ADMIN && roleName != RoleName.FLEET_MANAGER) {
            throw new RuntimeException("Only Admin or Fleet Manager can perform vehicle inspection");
        }

        if (!rental.getVehicle().getVehicleId().equals(vehicle.getVehicleId())) {
            throw new RuntimeException("Selected vehicle does not belong to this rental");
        }

        inspection.setInspectionType(vehicleInspectionDTO.getInspectionType());
        inspection.setInspectionDate(vehicleInspectionDTO.getInspectionDate());
        inspection.setFuelLevel(vehicleInspectionDTO.getFuelLevel());
        inspection.setMileage(vehicleInspectionDTO.getMileage());
        inspection.setNotes(vehicleInspectionDTO.getNotes());
        inspection.setVehicle(vehicle);
        inspection.setRental(rental);
        inspection.setInspectedBy(inspectedBy);

        vehicleInspectionRepository.save(inspection);

    }

    @Override
    @Transactional
    public void deleteVehicleInspection(Long inspectionId) {

        if (!vehicleInspectionRepository.existsById(inspectionId)) {
            throw new NotFoundException("Vehicle inspection not found");
        }
        vehicleInspectionRepository.deleteById(inspectionId);
    }
}
