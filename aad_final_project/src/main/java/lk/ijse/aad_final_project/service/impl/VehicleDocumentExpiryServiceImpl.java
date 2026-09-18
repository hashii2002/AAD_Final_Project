package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.VehicleDocumentDTO;
import lk.ijse.aad_final_project.entity.VehicleDocument;
import lk.ijse.aad_final_project.repository.VehicleDocumentRepository;
import lk.ijse.aad_final_project.service.VehicleDocumentExpiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VehicleDocumentExpiryServiceImpl
        implements VehicleDocumentExpiryService {


    private final VehicleDocumentRepository vehicleDocumentRepository;

    @Override
    @Scheduled(cron = "0 0 9 * * *")
    public void checkDocumentExpiry() {

        LocalDate today = LocalDate.now();

        LocalDate alertDate = today.plusDays(7);


        List<VehicleDocument> documents =
                vehicleDocumentRepository.findDocumentsExpiringSoon(
                        today,
                        alertDate
                );


        if (documents.isEmpty()) {

            log.info(
                    "Vehicle document expiry check completed. No documents expiring within 7 days."
            );

            return;
        }


        for (VehicleDocument document : documents) {

            long remainingDays =
                    ChronoUnit.DAYS.between(
                            today,
                            document.getExpiryDate()
                    );


            log.warn(
                    "Vehicle document expiry alert: " +
                            "Document ID={}, Vehicle ID={}, Document Type={}, " +
                            "Expiry Date={}, Remaining Days={}",
                    document.getDocumentId(),
                    document.getVehicle().getVehicleId(),
                    document.getDocumentType(),
                    document.getExpiryDate(),
                    remainingDays
            );
        }


        log.info(
                "{} vehicle document(s) are expiring within 7 days.",
                documents.size()
        );
    }


    /*
     * Returns documents that are currently within
     * the 7-day expiry warning period.
     */
    @Override
    public List<VehicleDocumentDTO> getExpiringDocuments() {

        LocalDate today = LocalDate.now();

        LocalDate alertDate = today.plusDays(7);


        List<VehicleDocument> documents =
                vehicleDocumentRepository.findDocumentsExpiringSoon(
                        today,
                        alertDate
                );


        List<VehicleDocumentDTO> dtoList =
                new ArrayList<>();


        for (VehicleDocument document : documents) {

            VehicleDocumentDTO dto =
                    new VehicleDocumentDTO();

            dto.setDocumentId(
                    document.getDocumentId()
            );

            dto.setVehicleId(
                    document.getVehicle().getVehicleId()
            );

            dto.setDocumentType(
                    document.getDocumentType()
            );

            dto.setDocumentNumber(
                    document.getDocumentNumber()
            );

            dto.setIssueDate(
                    document.getIssueDate()
            );

            dto.setExpiryDate(
                    document.getExpiryDate()
            );

            dto.setStatus(
                    document.getStatus()
            );

            dtoList.add(dto);
        }


        return dtoList;
    }
}