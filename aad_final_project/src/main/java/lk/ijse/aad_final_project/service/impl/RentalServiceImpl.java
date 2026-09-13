package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.RentalDTO;
import lk.ijse.aad_final_project.entity.*;
import lk.ijse.aad_final_project.enums.DriverOption;
import lk.ijse.aad_final_project.enums.RentalStatus;
import lk.ijse.aad_final_project.exception.DuplicateException;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
import lk.ijse.aad_final_project.repository.*;
import lk.ijse.aad_final_project.service.EmailService;
import lk.ijse.aad_final_project.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final RentalRateRepository rentalRateRepository;
    private final DriverRepository driverRepository;
    private final RentalDriverRepository rentalDriverRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public void saveRental(RentalDTO rentalDTO) {
        validateRentalDTO(rentalDTO);

        Customer customer = customerRepository.findById(rentalDTO.getCustomerId()).orElseThrow(() -> new NotFoundException("Customer not found"));

        Vehicle vehicle = vehicleRepository.findById(rentalDTO.getVehicleId()).orElseThrow(() -> new NotFoundException("Vehicle not found"));

        RentalRate rentalRate = resolveRentalRate(rentalDTO, vehicle);

        validateVehicleAvailability(vehicle.getVehicleId(), rentalDTO.getStartDate(), rentalDTO.getEndDate(), null);

        int rentalDays = calculateRentalDays(rentalDTO.getStartDate(), rentalDTO.getEndDate());
        double totalAmount = calculateTotalAmount(rentalDays, rentalRate);

        Rental rental = new Rental();
        rental.setStartDate(rentalDTO.getStartDate());
        rental.setEndDate(rentalDTO.getEndDate());
        rental.setRentalDays(rentalDays);
        rental.setPickupMileage(rentalDTO.getPickupMileage());
        rental.setReturnMileage(rentalDTO.getReturnMileage());
        rental.setDepositAmount(rentalDTO.getDepositAmount());
        rental.setStatus(rentalDTO.getStatus() != null ? rentalDTO.getStatus() : RentalStatus.PENDING);
        rental.setTotalAmount(totalAmount);

        DriverOption driverOption = rentalDTO.getDriverOption() != null ? rentalDTO.getDriverOption() : DriverOption.WITHOUT_DRIVER;
        rental.setDriverOption(driverOption);

        rental.setCustomer(customer);
        rental.setVehicle(vehicle);
        rental.setRentalRate(rentalRate);

        Rental savedRental = rentalRepository.save(rental);

        manageDriverAssignment(savedRental, driverOption, rentalDTO.getDriverId());

        if (customer.getUser() != null && customer.getUser().getEmail() != null) {
            String customerEmail = customer.getUser().getEmail();
            String customerName = customer.getUser().getFirstName();
            String vehicleNumber = vehicle.getVehicleNo();
            String endDateStr = rental.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            emailService.sendEmail(
                    customerEmail,
                    "Rental Booking Confirmation - Vehicle Rental System",
                    "Dear " + customerName + ",\n\n" +
                            "Your rental booking has been successfully placed.\n" +
                            "Vehicle Number: " + vehicleNumber + "\n" +
                            "End Date: " + endDateStr + "\n\n" +
                            "Thank you for choosing our service!"
            );
        }
    }

    @Override
    public List<RentalDTO> getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        List<RentalDTO> rentalDTOList = new ArrayList<>();

        for (Rental rental : rentals) {
            rentalDTOList.add(mapToRentalDTO(rental));
        }
        return rentalDTOList;
    }

    @Override
    public RentalDTO selectRental(Long rentalId) {
        if (rentalId == null) {
            throw new ValidationException("Rental ID is required");
        }

        Rental rental = rentalRepository.findById(rentalId).orElseThrow(() -> new NotFoundException("Rental record not found"));

        return mapToRentalDTO(rental);
    }

    @Override
    @Transactional
    public void updateRental(RentalDTO rentalDTO) {
        if (rentalDTO == null || rentalDTO.getRentalId() == null) {
            throw new ValidationException("Rental ID is required for update");
        }

        validateRentalDTO(rentalDTO);

        Rental rental = rentalRepository.findById(rentalDTO.getRentalId()).orElseThrow(() -> new NotFoundException("Rental record not found"));
        Customer customer = customerRepository.findById(rentalDTO.getCustomerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        Vehicle vehicle = vehicleRepository.findById(rentalDTO.getVehicleId()).orElseThrow(() -> new NotFoundException("Vehicle not found"));

        RentalRate rentalRate = resolveRentalRate(rentalDTO, vehicle);

        validateVehicleAvailability(vehicle.getVehicleId(), rentalDTO.getStartDate(), rentalDTO.getEndDate(), rental.getRentalId());

        int rentalDays = calculateRentalDays(rentalDTO.getStartDate(), rentalDTO.getEndDate());
        double totalAmount = calculateTotalAmount(rentalDays, rentalRate);

        rental.setStartDate(rentalDTO.getStartDate());
        rental.setEndDate(rentalDTO.getEndDate());
        rental.setRentalDays(rentalDays);
        rental.setPickupMileage(rentalDTO.getPickupMileage());
        rental.setReturnMileage(rentalDTO.getReturnMileage());
        rental.setDepositAmount(rentalDTO.getDepositAmount());

        if (rentalDTO.getStatus() != null) {
            rental.setStatus(rentalDTO.getStatus());
        }

        rental.setTotalAmount(totalAmount);

        DriverOption driverOption = rentalDTO.getDriverOption() != null ? rentalDTO.getDriverOption() : DriverOption.WITHOUT_DRIVER;
        rental.setDriverOption(driverOption);

        rental.setCustomer(customer);
        rental.setVehicle(vehicle);
        rental.setRentalRate(rentalRate);

        Rental updatedRental = rentalRepository.save(rental);

        manageDriverAssignment(updatedRental, driverOption, rentalDTO.getDriverId());
    }

    @Override
    @Transactional
    public void deleteRental(Long rentalId) {
        if (rentalId == null) {
            throw new ValidationException("Rental ID is required");
        }

        Rental rental = rentalRepository.findById(rentalId).orElseThrow(() -> new NotFoundException("Rental record not found"));

        rental.setStatus(RentalStatus.CANCELLED);
        rentalRepository.save(rental);
    }

    @Override
    public List<RentalDTO> getMyRentals(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }

        List<Rental> rentals = rentalRepository.findByCustomer_User_Username(username.trim());
        List<RentalDTO> rentalDTOList = new ArrayList<>();

        for (Rental rental : rentals) {
            rentalDTOList.add(mapToRentalDTO(rental));
        }

        return rentalDTOList;
    }

    private void validateRentalDTO(RentalDTO dto) {
        if (dto == null) {
            throw new ValidationException("Rental data is required");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new ValidationException("Start date and End date are required");
        }
        if (!dto.getEndDate().isAfter(dto.getStartDate())) {
            throw new ValidationException("End date must be after start date");
        }
        if (dto.getPickupMileage() == null || dto.getPickupMileage() < 0) {
            throw new ValidationException("Pickup mileage must be non-negative");
        }
        if (dto.getReturnMileage() != null && dto.getReturnMileage() < dto.getPickupMileage()) {
            throw new ValidationException("Return mileage cannot be less than pickup mileage");
        }
        if (dto.getDepositAmount() == null || dto.getDepositAmount() < 0) {
            throw new ValidationException("Deposit amount must be non-negative");
        }
        if (dto.getCustomerId() == null) {
            throw new ValidationException("Customer ID is required");
        }
        if (dto.getVehicleId() == null) {
            throw new ValidationException("Vehicle ID is required");
        }
    }

    private void validateVehicleAvailability(Long vehicleId, LocalDateTime startDate, LocalDateTime endDate, Long rentalId) {
        List<RentalStatus> excludedStatuses = List.of(RentalStatus.CANCELLED, RentalStatus.COMPLETED);
        boolean exists = rentalRepository.existsOverlappingRental(vehicleId, startDate, endDate, excludedStatuses, rentalId);
        if (exists) {
            throw new DuplicateException("Vehicle is already booked for the selected date range");
        }
    }

    private RentalRate resolveRentalRate(RentalDTO dto, Vehicle vehicle) {
        if (dto.getRentalRateId() != null) {
            return rentalRateRepository.findById(dto.getRentalRateId()).orElseThrow(() -> new NotFoundException("Specified rental rate not found"));
        }

        if (vehicle.getCategory() != null) {
            return rentalRateRepository.findByCategory_CategoryId(vehicle.getCategory().getCategoryId()).orElseThrow(() -> new NotFoundException("No rental rate defined for this vehicle's category"));
        }

        throw new ValidationException("Rental rate ID or category rate must be available");
    }

    private int calculateRentalDays(LocalDateTime startDate, LocalDateTime endDate) {
        long days = Duration.between(startDate, endDate).toDays();
        return days <= 0 ? 1 : (int) days;
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

    private void manageDriverAssignment(Rental rental, DriverOption driverOption, Long driverId) {
        List<RentalDriver> existingRentalDrivers = rental.getRentalDrivers();

        if (DriverOption.WITH_DRIVER.equals(driverOption)) {
            if (driverId == null) {
                throw new ValidationException("Driver ID is required when WITH_DRIVER option is selected");
            }

            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new NotFoundException("Driver not found"));

            if (existingRentalDrivers != null && !existingRentalDrivers.isEmpty()) {
                RentalDriver rentalDriver = existingRentalDrivers.get(0);
                rentalDriver.setDriver(driver);
                rentalDriverRepository.save(rentalDriver);
            } else {
                RentalDriver newRentalDriver = new RentalDriver();
                newRentalDriver.setRental(rental);
                newRentalDriver.setDriver(driver);
                rentalDriverRepository.save(newRentalDriver);
            }
        } else {
            if (existingRentalDrivers != null && !existingRentalDrivers.isEmpty()) {
                rentalDriverRepository.deleteAll(existingRentalDrivers);
                existingRentalDrivers.clear();
            }
        }
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

        if (rental.getCustomer() != null) {
            dto.setCustomerId(rental.getCustomer().getCustomerId());
        }
        if (rental.getVehicle() != null) {
            dto.setVehicleId(rental.getVehicle().getVehicleId());
        }
        if (rental.getRentalRate() != null) {
            dto.setRentalRateId(rental.getRentalRate().getRateId());
        }

        dto.setDriverOption(rental.getDriverOption());

        if (rental.getRentalDrivers() != null && !rental.getRentalDrivers().isEmpty()) {
            RentalDriver rentalDriver = rental.getRentalDrivers().get(0);
            if (rentalDriver != null && rentalDriver.getDriver() != null) {
                dto.setDriverId(rentalDriver.getDriver().getDriverId());
            }
        }

        return dto;
    }
}