package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.DriverDTO;
import lk.ijse.aad_final_project.entity.Driver;
import lk.ijse.aad_final_project.entity.User;
import lk.ijse.aad_final_project.repository.DriverRepository;
import lk.ijse.aad_final_project.repository.UserRepository;
import lk.ijse.aad_final_project.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;

    @Override
    public void saveDriver(DriverDTO driverDTO) {
        Optional<User> optionalUser =
                userRepository.findById(driverDTO.getUserId());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        Driver driver = new Driver();

        driver.setLicenseNo(driverDTO.getLicenseNo());
        driver.setStatus(driverDTO.getStatus());
        driver.setUser(user);

        driverRepository.save(driver);
    }

    @Override
    public List<DriverDTO> getAllDrivers() {
        List<Driver> drivers = driverRepository.findAll();

        List<DriverDTO> driverDTOList = new ArrayList<>();

        for (Driver driver : drivers) {

            DriverDTO driverDTO = new DriverDTO();

            driverDTO.setDriverId(driver.getDriverId());
            driverDTO.setUserId(driver.getUser().getUserId());
            driverDTO.setLicenseNo(driver.getLicenseNo());
            driverDTO.setStatus(driver.getStatus());

            driverDTOList.add(driverDTO);
        }

        return driverDTOList;
    }

    @Override
    public DriverDTO selectDriver(Long driverId) {
        Optional<Driver> optionalDriver =
                driverRepository.findById(driverId);

        if (optionalDriver.isEmpty()) {
            throw new RuntimeException("Driver not found");
        }

        Driver driver = optionalDriver.get();

        DriverDTO driverDTO = new DriverDTO();

        driverDTO.setDriverId(driver.getDriverId());
        driverDTO.setUserId(driver.getUser().getUserId());
        driverDTO.setLicenseNo(driver.getLicenseNo());
        driverDTO.setStatus(driver.getStatus());

        return driverDTO;
    }

    @Override
    public void updateDriver(DriverDTO driverDTO) {

        Optional<Driver> optionalDriver =
                driverRepository.findById(driverDTO.getDriverId());

        if (optionalDriver.isEmpty()) {
            throw new RuntimeException("Driver not found");
        }

        Driver driver = optionalDriver.get();

        driver.setLicenseNo(driverDTO.getLicenseNo());
        driver.setStatus(driverDTO.getStatus());

        driverRepository.save(driver);
    }

    @Override
    public void deleteDriver(Long driverId) {

        if (!driverRepository.existsById(driverId)) {
            throw new RuntimeException("Driver not found");
        }

        driverRepository.deleteById(driverId);
    }
}
