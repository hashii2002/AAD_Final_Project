package lk.ijse.aad_final_project.dto;

import lk.ijse.aad_final_project.enums.PaymentMethod;
import lk.ijse.aad_final_project.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PaymentDTO {
    private Long paymentId;

    private String paymentReference;

    private Double amount;

    private Double discount;

    private Double balance;

    private LocalDateTime paymentDate;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private Long rentalId;
}
