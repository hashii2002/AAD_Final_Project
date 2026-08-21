package lk.ijse.aad_final_project.dto;

import lk.ijse.aad_final_project.enums.RentalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalDTO {
    private Long rentalId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer rentalDays;
    private Double pickupMileage;
    private Double returnMileage;
    private Double depositAmount;
    private RentalStatus status;
    private Double totalAmount;
    private Long customerId;
    private Long vehicleId;
    private Long rentalRateId;
}
