package lk.ijse.aad_final_project.dto;

import lk.ijse.aad_final_project.enums.DocumentStatus;
import lk.ijse.aad_final_project.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDocumentDTO {
    private Long documentId;

    private Long vehicleId;

    private DocumentType documentType;

    private String documentNumber;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private DocumentStatus status;
}
