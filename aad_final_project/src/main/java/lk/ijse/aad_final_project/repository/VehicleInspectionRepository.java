package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.VehicleInspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleInspectionRepository extends JpaRepository<VehicleInspection,Long> {
}
