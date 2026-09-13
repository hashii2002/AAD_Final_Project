package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.MaintenanceRecordDTO;
import lk.ijse.aad_final_project.entity.MaintenanceRecord;
import lk.ijse.aad_final_project.entity.Vehicle;
import lk.ijse.aad_final_project.enums.MaintenanceStatus;
import lk.ijse.aad_final_project.exception.DuplicateException;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
import lk.ijse.aad_final_project.repository.MaintenanceRepository;
import lk.ijse.aad_final_project.repository.VehicleRepository;
import lk.ijse.aad_final_project.service.MaintenanceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public void saveMaintenanceRecord(MaintenanceRecordDTO maintenanceRecordDTO) {
        validateDTO(maintenanceRecordDTO);

        if (maintenanceRecordDTO.getVehicleId() == null) {
            throw new ValidationException("Vehicle ID is required");
        }

        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(maintenanceRecordDTO.getVehicleId());
        if (optionalVehicle.isEmpty()) {
            throw new NotFoundException("Vehicle not found");
        }

        Vehicle vehicle = optionalVehicle.get();

        boolean isDuplicate = maintenanceRepository.existsByVehicle_VehicleIdAndServiceDateAndStatusNot(
                vehicle.getVehicleId(),
                maintenanceRecordDTO.getServiceDate(),
                MaintenanceStatus.CANCELLED
        );

        if (isDuplicate) {
            throw new DuplicateException("A maintenance record already exists for this vehicle on the specified service date");
        }

        MaintenanceRecord record = new MaintenanceRecord();
        mapDTOToEntity(maintenanceRecordDTO, record, vehicle);

        maintenanceRepository.save(record);
    }

    @Override
    public List<MaintenanceRecordDTO> getAllMaintenanceRecords() {
        List<MaintenanceRecord> records = maintenanceRepository.findAll();
        List<MaintenanceRecordDTO> recordDTOList = new ArrayList<>();

        for (MaintenanceRecord record : records) {
            recordDTOList.add(mapEntityToDTO(record));
        }

        return recordDTOList;
    }

    @Override
    public MaintenanceRecordDTO selectMaintenanceRecord(Long maintenanceId) {
        if (maintenanceId == null) {
            throw new ValidationException("Maintenance ID is required");
        }

        Optional<MaintenanceRecord> optionalRecord = maintenanceRepository.findById(maintenanceId);
        if (optionalRecord.isEmpty()) {
            throw new NotFoundException("Maintenance record not found");
        }

        return mapEntityToDTO(optionalRecord.get());
    }

    @Override
    @Transactional
    public void updateMaintenanceRecord(MaintenanceRecordDTO maintenanceRecordDTO) {
        if (maintenanceRecordDTO == null) {
            throw new ValidationException("Maintenance record data is required");
        }
        if (maintenanceRecordDTO.getMaintenanceId() == null) {
            throw new ValidationException("Maintenance ID is required for update");
        }

        validateDTO(maintenanceRecordDTO);

        Optional<MaintenanceRecord> optionalRecord = maintenanceRepository.findById(maintenanceRecordDTO.getMaintenanceId());
        if (optionalRecord.isEmpty()) {
            throw new NotFoundException("Maintenance record not found");
        }

        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(maintenanceRecordDTO.getVehicleId());
        if (optionalVehicle.isEmpty()) {
            throw new NotFoundException("Vehicle not found");
        }

        MaintenanceRecord record = optionalRecord.get();

        if (record.getStatus() == MaintenanceStatus.CANCELLED) {
            throw new ValidationException("Cancelled maintenance record cannot be updated");
        }

        Vehicle vehicle = optionalVehicle.get();
        mapDTOToEntity(maintenanceRecordDTO, record, vehicle);

        maintenanceRepository.save(record);
    }

    @Override
    @Transactional
    public void deleteMaintenanceRecord(Long maintenanceId) {
        if (maintenanceId == null) {
            throw new ValidationException("Maintenance ID is required");
        }

        Optional<MaintenanceRecord> optionalRecord = maintenanceRepository.findById(maintenanceId);
        if (optionalRecord.isEmpty()) {
            throw new NotFoundException("Maintenance record not found");
        }

        MaintenanceRecord record = optionalRecord.get();
        if (record.getStatus() == MaintenanceStatus.CANCELLED) {
            throw new ValidationException("Maintenance record is already cancelled");
        }

        record.setStatus(MaintenanceStatus.CANCELLED);
        maintenanceRepository.save(record);
    }

    private void validateDTO(MaintenanceRecordDTO dto) {
        if (dto == null) {
            throw new ValidationException("Maintenance record data is required");
        }
        if (dto.getMaintenanceType() == null) {
            throw new ValidationException("Maintenance type is required");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new ValidationException("Description is required");
        }
        if (dto.getServiceDate() == null) {
            throw new ValidationException("Service date is required");
        }
        if (dto.getCost() == null || dto.getCost() < 0) {
            throw new ValidationException("Maintenance cost must be zero or positive");
        }
        if (dto.getStatus() == null) {
            throw new ValidationException("Maintenance status is required");
        }
        if (dto.getMileageAtService() == null || dto.getMileageAtService() < 0) {
            throw new ValidationException("Mileage at service must be zero or positive");
        }
        if (dto.getNextServiceDate() != null && dto.getNextServiceDate().isBefore(dto.getServiceDate())) {
            throw new ValidationException("Next service date cannot be before the service date");
        }
    }

    private void mapDTOToEntity(MaintenanceRecordDTO dto, MaintenanceRecord record, Vehicle vehicle) {
        record.setMaintenanceType(dto.getMaintenanceType());
        record.setDescription(dto.getDescription().trim());
        record.setServiceDate(dto.getServiceDate());
        record.setNextServiceDate(dto.getNextServiceDate());
        record.setCost(dto.getCost());
        record.setStatus(dto.getStatus());
        record.setMileageAtService(dto.getMileageAtService());
        record.setVehicle(vehicle);
    }

    private MaintenanceRecordDTO mapEntityToDTO(MaintenanceRecord record) {
        MaintenanceRecordDTO dto = new MaintenanceRecordDTO();
        dto.setMaintenanceId(record.getMaintenanceId());
        dto.setMaintenanceType(record.getMaintenanceType());
        dto.setDescription(record.getDescription());
        dto.setServiceDate(record.getServiceDate());
        dto.setNextServiceDate(record.getNextServiceDate());
        dto.setCost(record.getCost());
        dto.setStatus(record.getStatus());
        dto.setMileageAtService(record.getMileageAtService());

        if (record.getVehicle() != null) {
            dto.setVehicleId(record.getVehicle().getVehicleId());
        }

        return dto;
    }
}