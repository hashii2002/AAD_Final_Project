package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.VehicleInspection;
import lk.ijse.aad_final_project.enums.InspectionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleInspectionRepository extends JpaRepository<VehicleInspection,Long> {
    boolean existsByRental_RentalIdAndInspectionType(Long rentalId, InspectionType inspectionType);

    @Query("SELECT COUNT(i) > 0 FROM VehicleInspection i WHERE i.rental.rentalId = :rentalId AND i.inspectionType = :inspectionType AND i.inspectionId <> :inspectionId")
    boolean existsByRental_RentalIdAndInspectionTypeAndInspectionIdNot(
            @Param("rentalId") Long rentalId,
            @Param("inspectionType") InspectionType inspectionType,
            @Param("inspectionId") Long inspectionId);
}
