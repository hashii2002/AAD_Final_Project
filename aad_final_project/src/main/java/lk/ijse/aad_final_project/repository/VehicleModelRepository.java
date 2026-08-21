package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel,Long> {
}
