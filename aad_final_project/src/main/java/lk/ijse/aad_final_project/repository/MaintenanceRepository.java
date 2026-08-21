package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenanceRecord,Long> {
}
