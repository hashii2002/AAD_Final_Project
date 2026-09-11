package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Payment Reference is required")
    private String paymentReference;

    @NotNull(message = "Amount is required")
    private Double amount;

    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    private Double discount;

    private Double balance;

    private LocalDateTime paymentDate;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    @NotNull(message = "Rental ID is required")
    private Long rentalId;

    private Long customerId;

    private String customerName;
}
