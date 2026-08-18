package lk.ijse.aad_final_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class RentalRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_id")
    private Long rateId;

    @Column(name = "daily_rate", nullable = false)
    private Double dailyRate;

    @Column(name = "monthly_rate", nullable = false)
    private Double monthlyRate;

    @Column(name = "free_km_per_day", nullable = false)
    private Double freeKmPerDay;

    @Column(name = "extra_km_price", nullable = false)
    private Double extraKmPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private VehicleCategory category;

}
