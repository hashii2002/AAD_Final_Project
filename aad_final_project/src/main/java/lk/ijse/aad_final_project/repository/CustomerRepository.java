package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
