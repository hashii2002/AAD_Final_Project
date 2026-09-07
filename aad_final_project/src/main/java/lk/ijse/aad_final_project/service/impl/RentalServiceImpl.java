package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.RentalDTO;
import lk.ijse.aad_final_project.entity.*;
import lk.ijse.aad_final_project.enums.RentalStatus;
import lk.ijse.aad_final_project.repository.*;
import lk.ijse.aad_final_project.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

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

        // ------------ Rental Days Auto Calculation ----------------
        long calculatedDays = java.time.Duration.between(rentalDTO.getStartDate(), rentalDTO.getEndDate()).toDays();
        if (calculatedDays <= 0) {
            throw new RuntimeException("Invalid rental duration. End date must be after start date.");
        }
        int rentalDays = (int) calculatedDays;

        // ----------- Total Amount Auto Calculation ----------------
        double totalAmount = calculateTotalAmount(rentalDays, rentalRate);

        Rental rental = new Rental();

        rental.setStartDate(rentalDTO.getStartDate());
        rental.setEndDate(rentalDTO.getEndDate());
        rental.setRentalDays(rentalDays);
        rental.setPickupMileage(rentalDTO.getPickupMileage());
        rental.setReturnMileage(rentalDTO.getReturnMileage());
        rental.setDepositAmount(rentalDTO.getDepositAmount());
        rental.setStatus(rentalDTO.getStatus());
        rental.setTotalAmount(totalAmount);

        rental.setCustomer(customer);
        rental.setVehicle(vehicle);
        rental.setRentalRate(rentalRate);

        rentalRepository.save(rental);
    }

    private double calculateTotalAmount(int rentalDays, RentalRate rentalRate) {
        double dailyRate = rentalRate.getDailyRate();
        double monthlyRate = rentalRate.getMonthlyRate();

        if (rentalDays >= 30) {
            int months = rentalDays / 30;
            int remainingDays = rentalDays % 30;
            return (months * monthlyRate) + (remainingDays * dailyRate);
        } else {
            return rentalDays * dailyRate;
        }
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

        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);

        if (optionalRental.isEmpty()) {
            throw new RuntimeException("Rental not found");
        }

        Rental rental = optionalRental.get();

        rental.setStatus(RentalStatus.CANCELLED);

        rentalRepository.save(rental);
    }

    @Override
    public List<RentalDTO> getMyRentals(String username) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<User> optionalUser = userRepository.findByUsername(currentUsername);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Long currentUserId = optionalUser.get().getUserId();

        List<Rental> rentals = rentalRepository.findByCustomer_User_UserId(currentUserId);

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
}
