package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.dto.DriverDTO;
import lk.ijse.aad_final_project.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {

    Optional<Driver> findByUser_Username(String username);
}
