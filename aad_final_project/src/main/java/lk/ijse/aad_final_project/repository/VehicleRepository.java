package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {
    boolean existsByVehicleNo(String vehicleNo);

    @Query("SELECT COUNT(v) > 0 FROM Vehicle v WHERE v.vehicleNo = :vehicleNo AND v.vehicleId <> :vehicleId")
    boolean existsByVehicleNoAndVehicleIdNot(
            @Param("vehicleNo") String vehicleNo,
            @Param("vehicleId") Long vehicleId);

    @Query("""
            SELECT v
            FROM Vehicle v
            JOIN FETCH v.model m
            JOIN FETCH m.brand
            JOIN FETCH v.category
            WHERE v.status = lk.ijse.aad_final_project.enums.VehicleStatus.AVAILABLE
            """)
    List<Vehicle> findAvailableVehicles();

    // All vehicles -  AI Assistant
    @Query("""
            SELECT DISTINCT v
            FROM Vehicle v
            JOIN FETCH v.model m
            JOIN FETCH m.brand
            JOIN FETCH v.category
            ORDER BY v.vehicleId
            """)
    List<Vehicle> findAllVehiclesForAi();
}
