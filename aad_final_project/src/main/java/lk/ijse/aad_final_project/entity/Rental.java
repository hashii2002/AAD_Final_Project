package lk.ijse.aad_final_project.entity;

import jakarta.persistence.*;
import lk.ijse.aad_final_project.enums.RentalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Long rentalId;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "rental_days", nullable = false)
    private Integer rentalDays;

    @Column(name = "pickup_mileage", nullable = false)
    private Double pickupMileage;

    @Column(name = "return_mileage")
    private Double returnMileage;

    @Column(name = "deposit_amount", nullable = false)
    private Double depositAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RentalStatus status;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_rate_id", nullable = false)
    private RentalRate rentalRate;

    @OneToMany(mappedBy = "rental", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @OneToOne(mappedBy = "rental", fetch = FetchType.LAZY)
    private Invoice invoice;

    @OneToMany(mappedBy = "rental", fetch = FetchType.LAZY)
    private List<RentalDriver> rentalDrivers = new ArrayList<>();

    @OneToMany(mappedBy = "rental", fetch = FetchType.LAZY)
    private List<VehicleInspection> inspections = new ArrayList<>();

    @OneToOne(mappedBy = "rental", fetch = FetchType.LAZY)
    private Review review;
}
