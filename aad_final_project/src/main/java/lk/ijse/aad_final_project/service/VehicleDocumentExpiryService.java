package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.VehicleDocumentDTO;

import java.util.List;

public interface VehicleDocumentExpiryService {

    List<VehicleDocumentDTO> getExpiringDocuments();

    void checkDocumentExpiry();
}
