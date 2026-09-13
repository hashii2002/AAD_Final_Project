package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {

    Optional<Driver> findByUser_Username(String username);

    boolean existsByLicenseNo(String licenseNo);

    boolean existsByUser_UserId(Long userId);

    @Query("SELECT COUNT(d) > 0 FROM Driver d WHERE d.licenseNo = :licenseNo AND d.driverId <> :driverId")
    boolean existsByLicenseNoAndDriverIdNot(@Param("licenseNo") String licenseNo, @Param("driverId") Long driverId);

    @Query("SELECT COUNT(d) > 0 FROM Driver d WHERE d.user.userId = :userId AND d.driverId <> :driverId")
    boolean existsByUserIdAndDriverIdNot(@Param("userId") Long userId, @Param("driverId") Long driverId);
}
