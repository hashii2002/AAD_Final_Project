package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {
    boolean existsByVehicleNo(String vehicleNo);

    @Query("SELECT COUNT(v) > 0 FROM Vehicle v WHERE v.vehicleNo = :vehicleNo AND v.vehicleId <> :vehicleId")
    boolean existsByVehicleNoAndVehicleIdNot(
            @Param("vehicleNo") String vehicleNo,
            @Param("vehicleId") Long vehicleId
    );
}
