package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.VehicleBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleBrandRepository extends JpaRepository<VehicleBrand,Long> {
    boolean existsByBrandName(String brandName);

    @Query("SELECT COUNT(b) > 0 FROM VehicleBrand b WHERE b.brandName = :brandName AND b.brandId <> :brandId")
    boolean existsByBrandNameAndBrandIdNot(@Param("brandName") String brandName, @Param("brandId") Long brandId);
}
