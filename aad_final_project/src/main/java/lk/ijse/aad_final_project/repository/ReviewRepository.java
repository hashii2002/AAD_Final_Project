package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findByCustomer_User_Username(String username);

    Optional<Review> findByRental_RentalId(Long rentalId);
}
