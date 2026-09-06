package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.DriverDTO;

import java.util.List;

public interface DriverService {

    void saveDriver(DriverDTO driverDTO);

    List<DriverDTO> getAllDrivers();

    DriverDTO selectDriver(Long driverId);

    void updateDriver(DriverDTO driverDTO);

    void deleteDriver(Long driverId);

    DriverDTO getDriverByUserId(Long userId);
}
