package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
