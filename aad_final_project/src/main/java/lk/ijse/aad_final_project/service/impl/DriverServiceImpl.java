package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.DriverDTO;
import lk.ijse.aad_final_project.entity.Driver;
import lk.ijse.aad_final_project.entity.User;
import lk.ijse.aad_final_project.exception.DuplicateException;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
import lk.ijse.aad_final_project.repository.DriverRepository;
import lk.ijse.aad_final_project.repository.UserRepository;
import lk.ijse.aad_final_project.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lk.ijse.aad_final_project.dto.DriverProfileDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void saveDriver(DriverDTO driverDTO) {
        if (driverDTO == null) {
            throw new ValidationException("Driver data is required");
        }
        if (driverDTO.getUserId() == null) {
            throw new ValidationException("User ID is required");
        }
        if (driverDTO.getLicenseNo() == null || driverDTO.getLicenseNo().isBlank()) {
            throw new ValidationException("License number is required");
        }
        if (driverDTO.getStatus() == null) {
            throw new ValidationException("Driver status is required");
        }

        String licenseNo = driverDTO.getLicenseNo().trim();

        Optional<User> optionalUser = userRepository.findById(driverDTO.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        if (driverRepository.existsByUser_UserId(driverDTO.getUserId())) {
            throw new DuplicateException("User is already assigned to another driver profile");
        }

        if (driverRepository.existsByLicenseNo(licenseNo)) {
            throw new DuplicateException("License number already exists");
        }

        User user = optionalUser.get();

        Driver driver = new Driver();
        driver.setLicenseNo(licenseNo);
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
            if (driver.getUser() != null) {
                driverDTO.setUserId(driver.getUser().getUserId());
            }
            driverDTO.setLicenseNo(driver.getLicenseNo());
            driverDTO.setStatus(driver.getStatus());

            driverDTOList.add(driverDTO);
        }

        return driverDTOList;
    }

    @Override
    public DriverDTO selectDriver(Long driverId) {
        if (driverId == null) {
            throw new ValidationException("Driver ID is required");
        }

        Optional<Driver> optionalDriver = driverRepository.findById(driverId);
        if (optionalDriver.isEmpty()) {
            throw new NotFoundException("Driver not found");
        }

        Driver driver = optionalDriver.get();

        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setDriverId(driver.getDriverId());
        if (driver.getUser() != null) {
            driverDTO.setUserId(driver.getUser().getUserId());
        }
        driverDTO.setLicenseNo(driver.getLicenseNo());
        driverDTO.setStatus(driver.getStatus());

        return driverDTO;
    }

    @Override
    @Transactional
    public void updateDriver(DriverDTO driverDTO) {
        if (driverDTO == null) {
            throw new ValidationException("Driver data is required");
        }
        if (driverDTO.getDriverId() == null) {
            throw new ValidationException("Driver ID is required");
        }
        if (driverDTO.getUserId() == null) {
            throw new ValidationException("User ID is required");
        }
        if (driverDTO.getLicenseNo() == null || driverDTO.getLicenseNo().isBlank()) {
            throw new ValidationException("License number is required");
        }
        if (driverDTO.getStatus() == null) {
            throw new ValidationException("Driver status is required");
        }

        Optional<Driver> optionalDriver = driverRepository.findById(driverDTO.getDriverId());
        if (optionalDriver.isEmpty()) {
            throw new NotFoundException("Driver not found");
        }

        Optional<User> optionalUser = userRepository.findById(driverDTO.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        String licenseNo = driverDTO.getLicenseNo().trim();

        if (driverRepository.existsByLicenseNoAndDriverIdNot(licenseNo, driverDTO.getDriverId())) {
            throw new DuplicateException("License number already exists");
        }

        if (driverRepository.existsByUserIdAndDriverIdNot(driverDTO.getUserId(), driverDTO.getDriverId())) {
            throw new DuplicateException("User is already assigned to another driver profile");
        }

        Driver driver = optionalDriver.get();
        driver.setLicenseNo(licenseNo);
        driver.setStatus(driverDTO.getStatus());
        driver.setUser(optionalUser.get());

        driverRepository.save(driver);
    }

    @Override
    @Transactional
    public void deleteDriver(Long driverId) {
        if (driverId == null) {
            throw new ValidationException("Driver ID is required");
        }

        Optional<Driver> optionalDriver = driverRepository.findById(driverId);
        if (optionalDriver.isEmpty()) {
            throw new NotFoundException("Driver not found");
        }

        driverRepository.deleteById(driverId);
    }

    @Override
    public DriverDTO getDriverByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }

        String trimmedUsername = username.trim();

        Optional<Driver> optionalDriver = driverRepository.findByUser_Username(trimmedUsername);
        if (optionalDriver.isEmpty()) {
            throw new NotFoundException("Driver profile not found for the given username");
        }

        Driver driver = optionalDriver.get();

        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setDriverId(driver.getDriverId());
        if (driver.getUser() != null) {
            driverDTO.setUserId(driver.getUser().getUserId());
        }
        driverDTO.setLicenseNo(driver.getLicenseNo());
        driverDTO.setStatus(driver.getStatus());

        return driverDTO;
    }

    @Override
    public DriverProfileDTO getDriverProfile(String username) {

        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }

        Driver driver = driverRepository
                .findDriverWithUserByUsername(username.trim())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Driver profile not found for the given username"
                        )
                );

        DriverProfileDTO dto = new DriverProfileDTO();

        dto.setDriverId(driver.getDriverId());

        dto.setLicenseNo(driver.getLicenseNo());

        dto.setDriverStatus(driver.getStatus());

        if (driver.getUser() != null) {

            User user = driver.getUser();

            dto.setUserId(user.getUserId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setPhone(user.getPhone());
            dto.setUserStatus(user.getStatus());
        }

        return dto;
    }
}
