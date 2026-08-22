package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.MaintenanceRecordDTO;
import lk.ijse.aad_final_project.entity.MaintenanceRecord;
import lk.ijse.aad_final_project.entity.Vehicle;
import lk.ijse.aad_final_project.repository.MaintenanceRepository;
import lk.ijse.aad_final_project.repository.VehicleRepository;
import lk.ijse.aad_final_project.service.MaintenanceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {
    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public void saveMaintenanceRecord(MaintenanceRecordDTO maintenanceRecordDTO) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(maintenanceRecordDTO.getVehicleId());

        if (optionalVehicle.isEmpty()) {
            throw new RuntimeException("Vehicle not found");
        }

        Vehicle vehicle = optionalVehicle.get();

        MaintenanceRecord record = new MaintenanceRecord();
        record.setMaintenanceType(maintenanceRecordDTO.getMaintenanceType());
        record.setDescription(maintenanceRecordDTO.getDescription());
        record.setServiceDate(maintenanceRecordDTO.getServiceDate());
        record.setNextServiceDate(maintenanceRecordDTO.getNextServiceDate());
        record.setCost(maintenanceRecordDTO.getCost());
        record.setStatus(maintenanceRecordDTO.getStatus());
        record.setMileageAtService(maintenanceRecordDTO.getMileageAtService());
        record.setVehicle(vehicle);

        maintenanceRepository.save(record);
    }

    @Override
    public List<MaintenanceRecordDTO> getAllMaintenanceRecords() {
        List<MaintenanceRecord> records = maintenanceRepository.findAll();
        List<MaintenanceRecordDTO> recordDTOList = new ArrayList<>();

        for (MaintenanceRecord record : records) {
            MaintenanceRecordDTO dto = new MaintenanceRecordDTO();

            dto.setMaintenanceId(record.getMaintenanceId());
            dto.setMaintenanceType(record.getMaintenanceType());
            dto.setDescription(record.getDescription());
            dto.setServiceDate(record.getServiceDate());
            dto.setNextServiceDate(record.getNextServiceDate());
            dto.setCost(record.getCost());
            dto.setStatus(record.getStatus());
            dto.setMileageAtService(record.getMileageAtService());
            dto.setVehicleId(record.getVehicle().getVehicleId());

            recordDTOList.add(dto);
        }

        return recordDTOList;
    }

    @Override
    public MaintenanceRecordDTO selectMaintenanceRecord(Long maintenanceId) {
        Optional<MaintenanceRecord> optionalRecord = maintenanceRepository.findById(maintenanceId);

        if (optionalRecord.isEmpty()) {
            throw new RuntimeException("Maintenance record not found");
        }

        MaintenanceRecord record = optionalRecord.get();

        MaintenanceRecordDTO dto = new MaintenanceRecordDTO();
        dto.setMaintenanceId(record.getMaintenanceId());
        dto.setMaintenanceType(record.getMaintenanceType());
        dto.setDescription(record.getDescription());
        dto.setServiceDate(record.getServiceDate());
        dto.setNextServiceDate(record.getNextServiceDate());
        dto.setCost(record.getCost());
        dto.setStatus(record.getStatus());
        dto.setMileageAtService(record.getMileageAtService());
        dto.setVehicleId(record.getVehicle().getVehicleId());

        return dto;
    }

    @Override
    public void updateMaintenanceRecord(MaintenanceRecordDTO maintenanceRecordDTO) {
        Optional<MaintenanceRecord> optionalRecord =
                maintenanceRepository.findById(maintenanceRecordDTO.getMaintenanceId());

        if (optionalRecord.isEmpty()) {
            throw new RuntimeException("Maintenance record not found");
        }

        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(maintenanceRecordDTO.getVehicleId());

        if (optionalVehicle.isEmpty()) {
            throw new RuntimeException("Vehicle not found");
        }

        MaintenanceRecord record = optionalRecord.get();
        Vehicle vehicle = optionalVehicle.get();

        record.setMaintenanceType(maintenanceRecordDTO.getMaintenanceType());
        record.setDescription(maintenanceRecordDTO.getDescription());
        record.setServiceDate(maintenanceRecordDTO.getServiceDate());
        record.setNextServiceDate(maintenanceRecordDTO.getNextServiceDate());
        record.setCost(maintenanceRecordDTO.getCost());
        record.setStatus(maintenanceRecordDTO.getStatus());
        record.setMileageAtService(maintenanceRecordDTO.getMileageAtService());
        record.setVehicle(vehicle);

        maintenanceRepository.save(record);
    }

    @Override
    public void deleteMaintenanceRecord(Long maintenanceId) {
        if (!maintenanceRepository.existsById(maintenanceId)) {
            throw new RuntimeException("Maintenance record not found");
        }

        maintenanceRepository.deleteById(maintenanceId);
    }
}
