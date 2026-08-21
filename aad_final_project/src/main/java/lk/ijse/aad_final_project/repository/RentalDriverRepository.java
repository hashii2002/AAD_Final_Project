package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.RentalDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalDriverRepository extends JpaRepository<RentalDriver,Long> {
}
