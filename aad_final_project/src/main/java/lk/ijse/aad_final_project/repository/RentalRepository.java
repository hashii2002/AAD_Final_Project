package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental,Long> {

    List<Rental> findByCustomer_User_UserId(Long userId);
}
