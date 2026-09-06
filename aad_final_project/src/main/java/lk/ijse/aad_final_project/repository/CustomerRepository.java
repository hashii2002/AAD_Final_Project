package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findByUser_UserId(Long userId);

    Optional<Customer> findByUser_Username(String username);
}
