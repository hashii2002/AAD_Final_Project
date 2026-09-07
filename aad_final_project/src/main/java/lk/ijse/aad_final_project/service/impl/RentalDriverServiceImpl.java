package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.RentalDriverDTO;
import lk.ijse.aad_final_project.entity.Driver;
import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.entity.RentalDriver;
import lk.ijse.aad_final_project.repository.DriverRepository;
import lk.ijse.aad_final_project.repository.RentalDriverRepository;
import lk.ijse.aad_final_project.repository.RentalRepository;
import lk.ijse.aad_final_project.service.RentalDriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalDriverServiceImpl implements RentalDriverService {

    private final RentalDriverRepository rentalDriverRepository;
    private final RentalRepository rentalRepository;
    private final DriverRepository driverRepository;

    @Override
    public void saveRentalDriver(RentalDriverDTO rentalDriverDTO) {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalDriverDTO.getRentalId());

        if (optionalRental.isEmpty()) {
            throw new RuntimeException("Rental not found");
        }

        Optional<Driver> optionalDriver = driverRepository.findById(rentalDriverDTO.getDriverId());

        if (optionalDriver.isEmpty()) {
            throw new RuntimeException("Driver not found");
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

            RentalDriverDTO rentalDriverDTO = new RentalDriverDTO();

            rentalDriverDTO.setRentalDriverId(rentalDriver.getRentalDriverId());
            rentalDriverDTO.setRentalId(rentalDriver.getRental().getRentalId());
            rentalDriverDTO.setDriverId(rentalDriver.getDriver().getDriverId());
            rentalDriverDTOList.add(rentalDriverDTO);
        }
        return rentalDriverDTOList;
    }

    @Override
    public RentalDriverDTO selectRentalDriver(Long rentalDriverId) {
        Optional<RentalDriver> optionalRentalDriver = rentalDriverRepository.findById(rentalDriverId);

        if (optionalRentalDriver.isEmpty()) {
            throw new RuntimeException("Rental driver not found");
        }

        RentalDriver rentalDriver = optionalRentalDriver.get();

        RentalDriverDTO rentalDriverDTO = new RentalDriverDTO();

        rentalDriverDTO.setRentalDriverId(rentalDriver.getRentalDriverId());
        rentalDriverDTO.setRentalId(rentalDriver.getRental().getRentalId());
        rentalDriverDTO.setDriverId(rentalDriver.getDriver().getDriverId());

        return rentalDriverDTO;
    }

    @Override
    public void updateRentalDriver(RentalDriverDTO rentalDriverDTO) {
        Optional<RentalDriver> optionalRentalDriver = rentalDriverRepository.findById(rentalDriverDTO.getRentalDriverId());

        if (optionalRentalDriver.isEmpty()) {
            throw new RuntimeException("Rental driver not found");
        }

        Optional<Rental> optionalRental = rentalRepository.findById(rentalDriverDTO.getRentalId());

        if (optionalRental.isEmpty()) {
            throw new RuntimeException("Rental not found");
        }

        Optional<Driver> optionalDriver = driverRepository.findById(rentalDriverDTO.getDriverId());

        if (optionalDriver.isEmpty()) {
            throw new RuntimeException("Driver not found");
        }

        RentalDriver rentalDriver = optionalRentalDriver.get();

        Rental rental = optionalRental.get();
        Driver driver = optionalDriver.get();

        rentalDriver.setRental(rental);
        rentalDriver.setDriver(driver);

        rentalDriverRepository.save(rentalDriver);
    }

    @Override
    public void deleteRentalDriver(Long rentalDriverId) {

        if (!rentalDriverRepository.existsById(rentalDriverId)) {
            throw new RuntimeException("Rental driver not found");
        }

        rentalDriverRepository.deleteById(rentalDriverId);

    }

    @Override
    public List<RentalDriverDTO> getMyRentals(String username) {

        List<RentalDriver> rentalDrivers = rentalDriverRepository.findByDriver_User_Username(username);

        List<RentalDriverDTO> rentalDriverDTOList = new ArrayList<>();

        for (RentalDriver rentalDriver : rentalDrivers) {

            RentalDriverDTO rentalDriverDTO = new RentalDriverDTO();

            rentalDriverDTO.setRentalDriverId(rentalDriver.getRentalDriverId());
            rentalDriverDTO.setRentalId(rentalDriver.getRental().getRentalId());
            rentalDriverDTO.setDriverId(rentalDriver.getDriver().getDriverId());
            rentalDriverDTOList.add(rentalDriverDTO);
        }

        return rentalDriverDTOList;
    }
}
