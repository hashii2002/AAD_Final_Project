package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lk.ijse.aad_final_project.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class InvoiceDTO {

    private Long invoiceId;
    private LocalDateTime issueDate;
    private Double subTotal;

    @PositiveOrZero(message = "Discount cannot be negative")
    private Double discount;

    private Double totalAmount;
    private Double balance;
    private InvoiceStatus status;

    @NotNull(message = "Rental ID is required")
    private Long rentalId;

    private Long paymentId;
    private Long customerId;
    private String customerName;
}
