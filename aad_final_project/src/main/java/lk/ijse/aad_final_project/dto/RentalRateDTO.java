package lk.ijse.aad_final_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalRateDTO {

    private Long rateId;
    private Double dailyRate;
    private Double monthlyRate;
    private Double freeKmPerDay;
    private Double extraKmPrice;
    private Long categoryId;
}
