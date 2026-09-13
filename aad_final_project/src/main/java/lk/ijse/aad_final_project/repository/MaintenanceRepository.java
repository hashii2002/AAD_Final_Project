package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.MaintenanceRecord;
import lk.ijse.aad_final_project.enums.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenanceRecord,Long> {

    List<MaintenanceRecord> findByVehicle_VehicleId(Long vehicleId);

    List<MaintenanceRecord> findByStatus(MaintenanceStatus status);

    boolean existsByVehicle_VehicleIdAndServiceDateAndStatusNot(Long vehicleId, LocalDate serviceDate, MaintenanceStatus status);
}
