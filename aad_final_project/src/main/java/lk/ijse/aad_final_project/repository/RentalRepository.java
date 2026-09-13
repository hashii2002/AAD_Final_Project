package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.enums.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental,Long> {

    List<Rental> findByCustomer_User_UserId(Long userId);

    List<Rental> findByCustomer_User_Username(String username);

    @Query("SELECT COUNT(r) > 0 FROM Rental r WHERE r.vehicle.vehicleId = :vehicleId " +
            "AND r.status NOT IN (:excludedStatuses) " +
            "AND (:rentalId IS NULL OR r.rentalId != :rentalId) " +
            "AND r.startDate < :endDate AND r.endDate > :startDate")
    boolean existsOverlappingRental(@Param("vehicleId") Long vehicleId,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate,
                                    @Param("excludedStatuses") List<RentalStatus> excludedStatuses,
                                    @Param("rentalId") Long rentalId);

}
