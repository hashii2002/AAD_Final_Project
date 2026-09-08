package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    @Query("SELECT p FROM Payment p WHERE p.rental.customer.user.username = :username")
    List<Payment> findPaymentsByCustomerUsername(@Param("username") String username);

    List<Payment> findByRental_RentalId(Long rentalId);
}
