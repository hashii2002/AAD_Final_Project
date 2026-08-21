package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.VehicleBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleBrandRepository extends JpaRepository<VehicleBrand,Long> {
}
