package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.VehicleDocumentDTO;

import java.util.List;

public interface VehicleDocumentService {

    void saveVehicleDocument(VehicleDocumentDTO vehicleDocumentDTO);

    List<VehicleDocumentDTO> getAllVehicleDocuments();

    VehicleDocumentDTO selectVehicleDocument(Long documentId);

    void updateVehicleDocument(VehicleDocumentDTO vehicleDocumentDTO);

    void deleteVehicleDocument(Long documentId);
}
