package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.RentalRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalRateRepository extends JpaRepository<RentalRate,Long> {
    Optional<RentalRate> findByCategory_CategoryId(Long categoryId);

    boolean existsByCategory_CategoryId(Long categoryId);

    boolean existsByCategory_CategoryIdAndRateIdNot(Long categoryId, Long rateId);
}
