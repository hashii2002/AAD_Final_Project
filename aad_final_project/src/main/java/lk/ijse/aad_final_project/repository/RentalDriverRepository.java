package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.entity.RentalDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalDriverRepository extends JpaRepository<RentalDriver,Long> {

    @Query("SELECT rd.rental FROM RentalDriver rd WHERE rd.driver.user.username = :username")
    List<Rental> findRentalsByDriverUsername(@Param("username") String username);
}
