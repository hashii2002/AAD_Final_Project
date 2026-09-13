package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.RentalDTO;
import lk.ijse.aad_final_project.dto.RentalDriverDTO;
import lk.ijse.aad_final_project.entity.Driver;
import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.entity.RentalDriver;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.repository.DriverRepository;
import lk.ijse.aad_final_project.repository.RentalDriverRepository;
import lk.ijse.aad_final_project.repository.RentalRepository;
import lk.ijse.aad_final_project.service.RentalDriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

            RentalDriverDTO rentalDriverDTO = mapToRentalDriverDTO(rentalDriver);
            rentalDriverDTOList.add(rentalDriverDTO);
        }
        return rentalDriverDTOList;
    }

    @Override
    public RentalDriverDTO selectRentalDriver(Long rentalDriverId) {
        Optional<RentalDriver> optionalRentalDriver = rentalDriverRepository.findById(rentalDriverId);

        if (optionalRentalDriver.isEmpty()) {
            throw new NotFoundException("Rental driver not found");
        }

        RentalDriver rentalDriver = optionalRentalDriver.get();

        return mapToRentalDriverDTO(rentalDriver);
    }

    @Override
    @Transactional
    public void updateRentalDriver(RentalDriverDTO rentalDriverDTO) {
        Optional<RentalDriver> optionalRentalDriver = rentalDriverRepository.findById(rentalDriverDTO.getRentalDriverId());

        if (optionalRentalDriver.isEmpty()) {
            throw new NotFoundException("Rental driver not found");
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

        rentalDriver.setRental(rental);
        rentalDriver.setDriver(driver);

        rentalDriverRepository.save(rentalDriver);
    }

    @Override
    @Transactional
    public void deleteRentalDriver(Long rentalDriverId) {
        if (!rentalDriverRepository.existsById(rentalDriverId)) {
            throw new NotFoundException("Rental driver not found");
        }

        rentalDriverRepository.deleteById(rentalDriverId);

    }

    @Override
    public List<RentalDTO> getMyRentals(String username) {
        List<Rental> rentals = rentalDriverRepository.findRentalsByDriverUsername(username);

        List<RentalDTO> rentalDTOList = new ArrayList<>();

        for (Rental rental : rentals) {
            RentalDTO rentalDTO = new RentalDTO();
            rentalDTO.setRentalId(rental.getRentalId());
            rentalDTO.setStartDate(rental.getStartDate());
            rentalDTO.setEndDate(rental.getEndDate());
            rentalDTO.setRentalDays(rental.getRentalDays());
            rentalDTO.setPickupMileage(rental.getPickupMileage());
            rentalDTO.setReturnMileage(rental.getReturnMileage());
            rentalDTO.setDepositAmount(rental.getDepositAmount());
            rentalDTO.setStatus(rental.getStatus());
            rentalDTO.setTotalAmount(rental.getTotalAmount());
            rentalDTO.setDriverOption(rental.getDriverOption());
            rentalDTO.setCustomerId(rental.getCustomer().getCustomerId());
            rentalDTO.setVehicleId(rental.getVehicle().getVehicleId());
            rentalDTO.setRentalRateId(rental.getRentalRate().getRateId());

            if (rental.getRentalDrivers() != null && !rental.getRentalDrivers().isEmpty()) {
                rentalDTO.setDriverId(rental.getRentalDrivers().get(0).getDriver().getDriverId());
            }

            rentalDTOList.add(rentalDTO);
        }

        return rentalDTOList;
    }

    private RentalDriverDTO mapToRentalDriverDTO(RentalDriver rentalDriver) {
        RentalDriverDTO rentalDriverDTO = new RentalDriverDTO();

        rentalDriverDTO.setRentalDriverId(rentalDriver.getRentalDriverId());
        rentalDriverDTO.setRentalId(rentalDriver.getRental().getRentalId());

        if (rentalDriver.getDriver() != null) {
            Driver driver = rentalDriver.getDriver();
            rentalDriverDTO.setDriverId(driver.getDriverId());
            rentalDriverDTO.setDriverLicenseNo(driver.getLicenseNo());

            if (driver.getUser() != null) {
                rentalDriverDTO.setDriverName(driver.getUser().getFirstName() + " " + driver.getUser().getLastName());
                rentalDriverDTO.setDriverPhone(driver.getUser().getPhone());
            }
        }

        return rentalDriverDTO;
    }

}
