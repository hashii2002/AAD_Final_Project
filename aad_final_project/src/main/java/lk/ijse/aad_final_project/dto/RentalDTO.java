package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.NotNull;
import lk.ijse.aad_final_project.enums.DriverOption;
import lk.ijse.aad_final_project.enums.RentalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RentalDTO {
    private Long rentalId;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;
    private Integer rentalDays;

    @NotNull(message = "Pickup mileage is required")
    private Double pickupMileage;
    private Double returnMileage;

    @NotNull(message = "Deposit amount is required")
    private Double depositAmount;
    private RentalStatus status;
    private Double totalAmount;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;
    private Long rentalRateId;

    private DriverOption driverOption;
    private Long driverId;

    private VehicleInfoDTO vehicle;
}
