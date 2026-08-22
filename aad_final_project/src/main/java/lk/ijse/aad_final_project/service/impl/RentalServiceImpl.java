package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.RentalDTO;
import lk.ijse.aad_final_project.entity.Customer;
import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.entity.RentalRate;
import lk.ijse.aad_final_project.entity.Vehicle;
import lk.ijse.aad_final_project.repository.CustomerRepository;
import lk.ijse.aad_final_project.repository.RentalRateRepository;
import lk.ijse.aad_final_project.repository.RentalRepository;
import lk.ijse.aad_final_project.repository.VehicleRepository;
import lk.ijse.aad_final_project.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final RentalRateRepository rentalRateRepository;

    @Override
    public void saveRental(RentalDTO rentalDTO) {
        Optional<Customer> optionalCustomer = customerRepository.findById(rentalDTO.getCustomerId());

        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(rentalDTO.getVehicleId());

        if (optionalVehicle.isEmpty()) {
            throw new RuntimeException("Vehicle not found");
        }

        Optional<RentalRate> optionalRentalRate = rentalRateRepository.findById(rentalDTO.getRentalRateId());
        if (optionalRentalRate.isEmpty()) {
            throw new RuntimeException("Rental rate not found");
        }

        Customer customer = optionalCustomer.get();
        Vehicle vehicle = optionalVehicle.get();
        RentalRate rentalRate = optionalRentalRate.get();

        Rental rental = new Rental();

        rental.setStartDate(rentalDTO.getStartDate());
        rental.setEndDate(rentalDTO.getEndDate());
        rental.setRentalDays(rentalDTO.getRentalDays());
        rental.setPickupMileage(rentalDTO.getPickupMileage());
        rental.setReturnMileage(rentalDTO.getReturnMileage());
        rental.setDepositAmount(rentalDTO.getDepositAmount());
        rental.setStatus(rentalDTO.getStatus());
        rental.setTotalAmount(rentalDTO.getTotalAmount());

        rental.setCustomer(customer);
        rental.setVehicle(vehicle);
        rental.setRentalRate(rentalRate);

        rentalRepository.save(rental);
    }

    @Override
    public List<RentalDTO> getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();

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
            rentalDTO.setCustomerId(rental.getCustomer().getCustomerId());
            rentalDTO.setVehicleId(rental.getVehicle().getVehicleId());
            rentalDTO.setRentalRateId(rental.getRentalRate().getRateId());

            rentalDTOList.add(rentalDTO);
        }

        return rentalDTOList;

        }

    @Override
    public RentalDTO selectRental(Long rentalId) {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);

        if (optionalRental.isEmpty()) {
            throw new RuntimeException("Rental not found");
        }

        Rental rental = optionalRental.get();

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
        rentalDTO.setCustomerId(rental.getCustomer().getCustomerId());
        rentalDTO.setVehicleId(rental.getVehicle().getVehicleId());
        rentalDTO.setRentalRateId(rental.getRentalRate().getRateId());

        return rentalDTO;

    }

    @Override
    public void updateRental(RentalDTO rentalDTO) {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalDTO.getRentalId());

        if (optionalRental.isEmpty()) {
            throw new RuntimeException("Rental not found");
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(rentalDTO.getCustomerId());

        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(rentalDTO.getVehicleId());

        if (optionalVehicle.isEmpty()) {
            throw new RuntimeException("Vehicle not found");
        }
        Optional<RentalRate> optionalRentalRate = rentalRateRepository.findById(rentalDTO.getRentalRateId());

        if (optionalRentalRate.isEmpty()) {
            throw new RuntimeException("Rental rate not found");
        }

        Rental rental = optionalRental.get();

        Customer customer = optionalCustomer.get();
        Vehicle vehicle = optionalVehicle.get();
        RentalRate rentalRate = optionalRentalRate.get();

        rental.setStartDate(rentalDTO.getStartDate());
        rental.setEndDate(rentalDTO.getEndDate());
        rental.setRentalDays(rentalDTO.getRentalDays());
        rental.setPickupMileage(rentalDTO.getPickupMileage());
        rental.setReturnMileage(rentalDTO.getReturnMileage());
        rental.setDepositAmount(rentalDTO.getDepositAmount());
        rental.setStatus(rentalDTO.getStatus());
        rental.setTotalAmount(rentalDTO.getTotalAmount());

        rental.setCustomer(customer);
        rental.setVehicle(vehicle);
        rental.setRentalRate(rentalRate);

        rentalRepository.save(rental);

    }

    @Override
    public void deleteRental(Long rentalId) {
        if (!rentalRepository.existsById(rentalId)) {
            throw new RuntimeException("Rental not found");
        }

        rentalRepository.deleteById(rentalId);
    }
}
