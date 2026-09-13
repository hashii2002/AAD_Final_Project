package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.VehicleDocumentDTO;
import lk.ijse.aad_final_project.entity.Vehicle;
import lk.ijse.aad_final_project.entity.VehicleDocument;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.repository.VehicleDocumentRepository;
import lk.ijse.aad_final_project.repository.VehicleRepository;
import lk.ijse.aad_final_project.service.VehicleDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleDocumentServiceImpl implements VehicleDocumentService {
    private final VehicleDocumentRepository vehicleDocumentRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public void saveVehicleDocument(VehicleDocumentDTO vehicleDocumentDTO) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleDocumentDTO.getVehicleId());

        if (optionalVehicle.isEmpty()) {
            throw new NotFoundException("Vehicle not found");
        }

        Vehicle vehicle = optionalVehicle.get();

        VehicleDocument vehicleDocument = new VehicleDocument();

        vehicleDocument.setVehicle(vehicle);
        vehicleDocument.setDocumentType(vehicleDocumentDTO.getDocumentType());
        vehicleDocument.setDocumentNumber(vehicleDocumentDTO.getDocumentNumber());
        vehicleDocument.setIssueDate(vehicleDocumentDTO.getIssueDate());
        vehicleDocument.setExpiryDate(vehicleDocumentDTO.getExpiryDate());
        vehicleDocument.setStatus(vehicleDocumentDTO.getStatus());

        vehicleDocumentRepository.save(vehicleDocument);
    }

    @Override
    public List<VehicleDocumentDTO> getAllVehicleDocuments() {
        List<VehicleDocument> vehicleDocuments = vehicleDocumentRepository.findAll();

        List<VehicleDocumentDTO> vehicleDocumentDTOList = new ArrayList<>();

        for (VehicleDocument vehicleDocument : vehicleDocuments) {

            VehicleDocumentDTO vehicleDocumentDTO = new VehicleDocumentDTO();

            vehicleDocumentDTO.setDocumentId(vehicleDocument.getDocumentId());
            vehicleDocumentDTO.setVehicleId(vehicleDocument.getVehicle().getVehicleId());
            vehicleDocumentDTO.setDocumentType(vehicleDocument.getDocumentType());
            vehicleDocumentDTO.setDocumentNumber(vehicleDocument.getDocumentNumber());
            vehicleDocumentDTO.setIssueDate(vehicleDocument.getIssueDate());
            vehicleDocumentDTO.setExpiryDate(vehicleDocument.getExpiryDate());
            vehicleDocumentDTO.setStatus(vehicleDocument.getStatus());

            vehicleDocumentDTOList.add(vehicleDocumentDTO);
        }

        return vehicleDocumentDTOList;
    }

    @Override
    public VehicleDocumentDTO selectVehicleDocument(Long documentId) {
        Optional<VehicleDocument> optionalVehicleDocument = vehicleDocumentRepository.findById(documentId);

        if (optionalVehicleDocument.isEmpty()) {
            throw new NotFoundException("Vehicle document not found");
        }

        VehicleDocument vehicleDocument = optionalVehicleDocument.get();

        VehicleDocumentDTO vehicleDocumentDTO = new VehicleDocumentDTO();

        vehicleDocumentDTO.setDocumentId(vehicleDocument.getDocumentId());
        vehicleDocumentDTO.setVehicleId(vehicleDocument.getVehicle().getVehicleId());
        vehicleDocumentDTO.setDocumentType(vehicleDocument.getDocumentType());
        vehicleDocumentDTO.setDocumentNumber(vehicleDocument.getDocumentNumber());
        vehicleDocumentDTO.setIssueDate(vehicleDocument.getIssueDate());
        vehicleDocumentDTO.setExpiryDate(vehicleDocument.getExpiryDate());
        vehicleDocumentDTO.setStatus(vehicleDocument.getStatus());

        return vehicleDocumentDTO;
    }

    @Override
    @Transactional
    public void updateVehicleDocument(VehicleDocumentDTO vehicleDocumentDTO) {

        Optional<VehicleDocument> optionalVehicleDocument = vehicleDocumentRepository.findById(vehicleDocumentDTO.getDocumentId());
        if (optionalVehicleDocument.isEmpty()) {
            throw new NotFoundException("Vehicle document not found");
        }

        VehicleDocument vehicleDocument = optionalVehicleDocument.get();
        vehicleDocument.setDocumentType(vehicleDocumentDTO.getDocumentType());
        vehicleDocument.setDocumentNumber(vehicleDocumentDTO.getDocumentNumber());
        vehicleDocument.setIssueDate(vehicleDocumentDTO.getIssueDate());
        vehicleDocument.setExpiryDate(vehicleDocumentDTO.getExpiryDate());
        vehicleDocument.setStatus(vehicleDocumentDTO.getStatus());

        vehicleDocumentRepository.save(vehicleDocument);
    }

    @Override
    @Transactional
    public void deleteVehicleDocument(Long documentId) {

        if (!vehicleDocumentRepository.existsById(documentId)) {
            throw new NotFoundException("Vehicle document not found");
        }
        vehicleDocumentRepository.deleteById(documentId);

    }
}
