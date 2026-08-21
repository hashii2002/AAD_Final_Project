package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.RentalRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRateRepository extends JpaRepository<RentalRate,Long> {
}
