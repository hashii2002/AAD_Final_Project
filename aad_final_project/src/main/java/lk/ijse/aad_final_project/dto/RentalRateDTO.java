package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RentalRateDTO {

    private Long rateId;

    @NotNull(message = "Daily rate is required")
    private Double dailyRate;

    @NotNull(message = "Monthly rate is required")
    private Double monthlyRate;

    @NotNull(message = "Free KM per day is required")
    private Double freeKmPerDay;

    @NotNull(message = "Extra KM price is required")
    private Double extraKmPrice;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
