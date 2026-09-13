package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {

    @Query("SELECT i FROM Invoice i WHERE i.rental.customer.user.username = :username")
    List<Invoice> findInvoicesByCustomerUsername(@Param("username") String username);

    Optional<Invoice> findByRental_RentalId(Long rentalId);

    boolean existsByRental_RentalId(Long rentalId);
}
