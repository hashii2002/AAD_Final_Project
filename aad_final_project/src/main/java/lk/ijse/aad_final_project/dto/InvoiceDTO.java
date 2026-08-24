package lk.ijse.aad_final_project.dto;

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
    private Double discount;
    private Double totalAmount;
    private Double balance;
    private InvoiceStatus status;
    private Long rentalId;
}
