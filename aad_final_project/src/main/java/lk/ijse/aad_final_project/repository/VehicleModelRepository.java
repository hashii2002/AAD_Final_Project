package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel,Long> {

    boolean existsByModelNameAndBrand_BrandId(String modelName, Long brandId);

    @Query("SELECT COUNT(m) > 0 FROM VehicleModel m WHERE m.modelName = :modelName AND m.brand.brandId = :brandId AND m.modelId <> :modelId")
    boolean existsByModelNameAndBrand_BrandIdAndModelIdNot(
            @Param("modelName") String modelName,
            @Param("brandId") Long brandId,
            @Param("modelId") Long modelId
    );
}
