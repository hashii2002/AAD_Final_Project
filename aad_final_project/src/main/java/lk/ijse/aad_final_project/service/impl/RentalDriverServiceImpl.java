package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.RentalDTO;
import lk.ijse.aad_final_project.dto.RentalDriverDTO;
import lk.ijse.aad_final_project.entity.Driver;
import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.entity.RentalDriver;
import lk.ijse.aad_final_project.exception.DuplicateException;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
import lk.ijse.aad_final_project.repository.DriverRepository;
import lk.ijse.aad_final_project.repository.RentalDriverRepository;
import lk.ijse.aad_final_project.repository.RentalRepository;
import lk.ijse.aad_final_project.service.RentalDriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lk.ijse.aad_final_project.dto.VehicleInfoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentalDriverServiceImpl implements RentalDriverService {

    private final RentalDriverRepository rentalDriverRepository;
    private final RentalRepository rentalRepository;
    private final DriverRepository driverRepository;

    @Override
    @Transactional
    public void saveRentalDriver(RentalDriverDTO rentalDriverDTO) {
        validateRentalDriverDTO(rentalDriverDTO);

        Optional<Rental> optionalRental = rentalRepository.findById(rentalDriverDTO.getRentalId());
        if (optionalRental.isEmpty()) {
            throw new NotFoundException("Rental not found");
        }

        Optional<Driver> optionalDriver = driverRepository.findById(rentalDriverDTO.getDriverId());
        if (optionalDriver.isEmpty()) {
            throw new NotFoundException("Driver not found");
        }

        Rental rental = optionalRental.get();
        Driver driver = optionalDriver.get();

        if (rentalDriverRepository.existsByRental_RentalIdAndDriver_DriverId(rental.getRentalId(), driver.getDriverId())) {
            throw new DuplicateException("This driver is already assigned to the specified rental");
        }

        RentalDriver rentalDriver = new RentalDriver();
        rentalDriver.setRental(rental);
        rentalDriver.setDriver(driver);

        rentalDriverRepository.save(rentalDriver);
    }

    @Override
    public List<RentalDriverDTO> getAllRentalDrivers() {
        List<RentalDriver> rentalDrivers = rentalDriverRepository.findAll();
        List<RentalDriverDTO> rentalDriverDTOList = new ArrayList<>();

        for (RentalDriver rentalDriver : rentalDrivers) {
            rentalDriverDTOList.add(mapToRentalDriverDTO(rentalDriver));
        }
        return rentalDriverDTOList;
    }

    @Override
    public RentalDriverDTO selectRentalDriver(Long rentalDriverId) {
        if (rentalDriverId == null) {
            throw new ValidationException("Rental Driver ID is required");
        }

        Optional<RentalDriver> optionalRentalDriver = rentalDriverRepository.findById(rentalDriverId);
        if (optionalRentalDriver.isEmpty()) {
            throw new NotFoundException("Rental driver record not found");
        }

        return mapToRentalDriverDTO(optionalRentalDriver.get());
    }

    @Override
    @Transactional
    public void updateRentalDriver(RentalDriverDTO rentalDriverDTO) {
        if (rentalDriverDTO == null) {
            throw new ValidationException("Rental driver data is required");
        }
        if (rentalDriverDTO.getRentalDriverId() == null) {
            throw new ValidationException("Rental Driver ID is required for update");
        }

        validateRentalDriverDTO(rentalDriverDTO);

        Optional<RentalDriver> optionalRentalDriver = rentalDriverRepository.findById(rentalDriverDTO.getRentalDriverId());
        if (optionalRentalDriver.isEmpty()) {
            throw new NotFoundException("Rental driver record not found");
        }

        Optional<Rental> optionalRental = rentalRepository.findById(rentalDriverDTO.getRentalId());
        if (optionalRental.isEmpty()) {
            throw new NotFoundException("Rental not found");
        }

        Optional<Driver> optionalDriver = driverRepository.findById(rentalDriverDTO.getDriverId());
        if (optionalDriver.isEmpty()) {
            throw new NotFoundException("Driver not found");
        }

        RentalDriver rentalDriver = optionalRentalDriver.get();
        Rental rental = optionalRental.get();
        Driver driver = optionalDriver.get();

        if (rentalDriverRepository.existsByRental_RentalIdAndDriver_DriverIdAndRentalDriverIdNot(
                rental.getRentalId(), driver.getDriverId(), rentalDriver.getRentalDriverId())) {
            throw new DuplicateException("This driver is already assigned to the specified rental");
        }

        rentalDriver.setRental(rental);
        rentalDriver.setDriver(driver);

        rentalDriverRepository.save(rentalDriver);
    }

    @Override
    @Transactional
    public void deleteRentalDriver(Long rentalDriverId) {
        if (rentalDriverId == null) {
            throw new ValidationException("Rental Driver ID is required");
        }

        if (!rentalDriverRepository.existsById(rentalDriverId)) {
            throw new NotFoundException("Rental driver record not found");
        }

        rentalDriverRepository.deleteById(rentalDriverId);
    }

    @Override
    public List<RentalDTO> getMyRentals(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }

        List<Rental> rentals = rentalDriverRepository.findRentalsByDriverUsername(username.trim());
        List<RentalDTO> rentalDTOList = new ArrayList<>();

        for (Rental rental : rentals) {
            rentalDTOList.add(mapToRentalDTO(rental));
        }

        return rentalDTOList;
    }

    private void validateRentalDriverDTO(RentalDriverDTO dto) {
        if (dto == null) {
            throw new ValidationException("Rental driver data is required");
        }
        if (dto.getRentalId() == null) {
            throw new ValidationException("Rental ID is required");
        }
        if (dto.getDriverId() == null) {
            throw new ValidationException("Driver ID is required");
        }
    }

    private RentalDriverDTO mapToRentalDriverDTO(RentalDriver rentalDriver) {
        RentalDriverDTO dto = new RentalDriverDTO();
        dto.setRentalDriverId(rentalDriver.getRentalDriverId());

        if (rentalDriver.getRental() != null) {
            dto.setRentalId(rentalDriver.getRental().getRentalId());
        }

        if (rentalDriver.getDriver() != null) {
            Driver driver = rentalDriver.getDriver();
            dto.setDriverId(driver.getDriverId());
            dto.setDriverLicenseNo(driver.getLicenseNo());

            if (driver.getUser() != null) {
                String firstName = driver.getUser().getFirstName() != null ? driver.getUser().getFirstName() : "";
                String lastName = driver.getUser().getLastName() != null ? driver.getUser().getLastName() : "";
                dto.setDriverName((firstName + " " + lastName).trim());
                dto.setDriverPhone(driver.getUser().getPhone());
            }
        }

        return dto;
    }

    private RentalDTO mapToRentalDTO(Rental rental) {

        RentalDTO dto = new RentalDTO();

        dto.setRentalId(rental.getRentalId());
        dto.setStartDate(rental.getStartDate());
        dto.setEndDate(rental.getEndDate());
        dto.setRentalDays(rental.getRentalDays());
        dto.setPickupMileage(rental.getPickupMileage());
        dto.setReturnMileage(rental.getReturnMileage());
        dto.setDepositAmount(rental.getDepositAmount());
        dto.setStatus(rental.getStatus());
        dto.setTotalAmount(rental.getTotalAmount());
        dto.setDriverOption(rental.getDriverOption());

        if (rental.getCustomer() != null) {
            dto.setCustomerId(rental.getCustomer().getCustomerId());
        }

        if (rental.getVehicle() != null) {

            dto.setVehicleId(rental.getVehicle().getVehicleId());
            VehicleInfoDTO vehicleInfoDTO = new VehicleInfoDTO();

            vehicleInfoDTO.setVehicleId(rental.getVehicle().getVehicleId());
            vehicleInfoDTO.setVehicleNo(rental.getVehicle().getVehicleNo());
            vehicleInfoDTO.setColor(rental.getVehicle().getColor());
            vehicleInfoDTO.setYear(rental.getVehicle().getYear());

            if (rental.getVehicle().getModel() != null) {

                vehicleInfoDTO.setModelName(rental.getVehicle().getModel().getModelName());
                vehicleInfoDTO.setFuelType(rental.getVehicle().getModel().getFuelType() != null ? rental.getVehicle().getModel().getFuelType().name() : null);
                vehicleInfoDTO.setSeatingCapacity(rental.getVehicle().getModel().getSeatingCapacity());
                vehicleInfoDTO.setTransmissionType(rental.getVehicle().getModel().getTransmissionType() != null ? rental.getVehicle().getModel().getTransmissionType().name() : null);

                if (rental.getVehicle().getModel().getBrand() != null) {
                    vehicleInfoDTO.setBrandName(rental.getVehicle().getModel().getBrand().getBrandName());
                }
            }

            if (rental.getVehicle().getCategory() != null) {
                vehicleInfoDTO.setCategoryName(rental.getVehicle().getCategory().getCategory() != null ? rental.getVehicle().getCategory().getCategory().name() : null);
            }
            dto.setVehicle(vehicleInfoDTO);
        }

        if (rental.getRentalRate() != null) {
            dto.setRentalRateId(rental.getRentalRate().getRateId());
        }

        if (rental.getRentalDrivers() != null && !rental.getRentalDrivers().isEmpty()) {
            RentalDriver firstRentalDriver = rental.getRentalDrivers().get(0);

            if (firstRentalDriver != null && firstRentalDriver.getDriver() != null) {
                dto.setDriverId(firstRentalDriver.getDriver().getDriverId());
            }
        }
        return dto;
    }
}
