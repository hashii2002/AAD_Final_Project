package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.VehicleCategory;
import lk.ijse.aad_final_project.enums.Categoryname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory,Long> {
    boolean existsByCategory(Categoryname category);

    @Query("SELECT COUNT(c) > 0 FROM VehicleCategory c WHERE c.category = :category AND c.categoryId <> :categoryId")
    boolean existsByCategoryAndCategoryIdNot(@Param("category") Categoryname category, @Param("categoryId") Long categoryId);
}
