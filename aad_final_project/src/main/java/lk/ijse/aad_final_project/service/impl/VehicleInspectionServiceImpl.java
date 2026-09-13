package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.VehicleInspectionDTO;
import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.entity.User;
import lk.ijse.aad_final_project.entity.Vehicle;
import lk.ijse.aad_final_project.entity.VehicleInspection;
import lk.ijse.aad_final_project.enums.RoleName;
import lk.ijse.aad_final_project.exception.DuplicateException;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
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
        if (vehicleInspectionDTO == null) {
            throw new ValidationException("Vehicle inspection data is required");
        }
        if (vehicleInspectionDTO.getVehicleId() == null) {
            throw new ValidationException("Vehicle ID is required");
        }
        if (vehicleInspectionDTO.getRentalId() == null) {
            throw new ValidationException("Rental ID is required");
        }
        if (vehicleInspectionDTO.getInspectedById() == null) {
            throw new ValidationException("Inspected by user ID is required");
        }
        if (vehicleInspectionDTO.getInspectionType() == null) {
            throw new ValidationException("Inspection type is required");
        }
        if (vehicleInspectionDTO.getFuelLevel() == null || vehicleInspectionDTO.getFuelLevel().isBlank()) {
            throw new ValidationException("Fuel level is required");
        }
        if (vehicleInspectionDTO.getMileage() == null || vehicleInspectionDTO.getMileage() < 0) {
            throw new ValidationException("Valid mileage is required");
        }

        if (vehicleInspectionRepository.existsByRental_RentalIdAndInspectionType(vehicleInspectionDTO.getRentalId(), vehicleInspectionDTO.getInspectionType())) {
            throw new DuplicateException("Inspection of type " + vehicleInspectionDTO.getInspectionType() + " already exists for this rental");
        }

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
            throw new ValidationException("Only Admin or Fleet Manager can perform vehicle inspection");
        }

        if (!rental.getVehicle().getVehicleId().equals(vehicle.getVehicleId())) {
            throw new ValidationException("Selected vehicle does not belong to this rental");
        }

        String notes = vehicleInspectionDTO.getNotes() != null ? vehicleInspectionDTO.getNotes().trim() : null;

        VehicleInspection vehicleInspection = new VehicleInspection();
        vehicleInspection.setInspectionType(vehicleInspectionDTO.getInspectionType());
        vehicleInspection.setInspectionDate(vehicleInspectionDTO.getInspectionDate() != null ? vehicleInspectionDTO.getInspectionDate() : LocalDateTime.now());
        vehicleInspection.setFuelLevel(vehicleInspectionDTO.getFuelLevel().trim());
        vehicleInspection.setMileage(vehicleInspectionDTO.getMileage());
        vehicleInspection.setNotes(notes);
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
        if (inspectionId == null) {
            throw new ValidationException("Inspection ID is required");
        }

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
        if (vehicleInspectionDTO == null) {
            throw new ValidationException("Vehicle inspection data is required");
        }
        if (vehicleInspectionDTO.getInspectionId() == null) {
            throw new ValidationException("Inspection ID is required");
        }
        if (vehicleInspectionDTO.getVehicleId() == null) {
            throw new ValidationException("Vehicle ID is required");
        }
        if (vehicleInspectionDTO.getRentalId() == null) {
            throw new ValidationException("Rental ID is required");
        }
        if (vehicleInspectionDTO.getInspectedById() == null) {
            throw new ValidationException("Inspected by user ID is required");
        }
        if (vehicleInspectionDTO.getInspectionType() == null) {
            throw new ValidationException("Inspection type is required");
        }
        if (vehicleInspectionDTO.getFuelLevel() == null || vehicleInspectionDTO.getFuelLevel().isBlank()) {
            throw new ValidationException("Fuel level is required");
        }
        if (vehicleInspectionDTO.getMileage() == null || vehicleInspectionDTO.getMileage() < 0) {
            throw new ValidationException("Valid mileage is required");
        }

        Optional<VehicleInspection> optionalInspection = vehicleInspectionRepository.findById(vehicleInspectionDTO.getInspectionId());
        if (optionalInspection.isEmpty()) {
            throw new NotFoundException("Vehicle inspection not found");
        }

        if (vehicleInspectionRepository.existsByRental_RentalIdAndInspectionTypeAndInspectionIdNot(
                vehicleInspectionDTO.getRentalId(), vehicleInspectionDTO.getInspectionType(), vehicleInspectionDTO.getInspectionId())) {
            throw new DuplicateException("Inspection of type " + vehicleInspectionDTO.getInspectionType() + " already exists for this rental");
        }

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
            throw new ValidationException("Only Admin or Fleet Manager can perform vehicle inspection");
        }

        if (!rental.getVehicle().getVehicleId().equals(vehicle.getVehicleId())) {
            throw new ValidationException("Selected vehicle does not belong to this rental");
        }

        String notes = vehicleInspectionDTO.getNotes() != null ? vehicleInspectionDTO.getNotes().trim() : null;

        VehicleInspection inspection = optionalInspection.get();
        inspection.setInspectionType(vehicleInspectionDTO.getInspectionType());
        inspection.setInspectionDate(vehicleInspectionDTO.getInspectionDate() != null ? vehicleInspectionDTO.getInspectionDate() : inspection.getInspectionDate());
        inspection.setFuelLevel(vehicleInspectionDTO.getFuelLevel().trim());
        inspection.setMileage(vehicleInspectionDTO.getMileage());
        inspection.setNotes(notes);
        inspection.setVehicle(vehicle);
        inspection.setRental(rental);
        inspection.setInspectedBy(inspectedBy);

        vehicleInspectionRepository.save(inspection);
    }

    @Override
    @Transactional
    public void deleteVehicleInspection(Long inspectionId) {
        if (inspectionId == null) {
            throw new ValidationException("Inspection ID is required");
        }

        Optional<VehicleInspection> optionalInspection = vehicleInspectionRepository.findById(inspectionId);
        if (optionalInspection.isEmpty()) {
            throw new NotFoundException("Vehicle inspection not found");
        }

        vehicleInspectionRepository.deleteById(inspectionId);
    }
}