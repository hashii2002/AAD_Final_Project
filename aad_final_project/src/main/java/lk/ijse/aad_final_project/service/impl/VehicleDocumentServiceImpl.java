package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.VehicleDocumentDTO;
import lk.ijse.aad_final_project.entity.Vehicle;
import lk.ijse.aad_final_project.entity.VehicleDocument;
import lk.ijse.aad_final_project.exception.DuplicateException;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
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
        if (vehicleDocumentDTO == null) {
            throw new ValidationException("Vehicle document data is required");
        }
        if (vehicleDocumentDTO.getVehicleId() == null) {
            throw new ValidationException("Vehicle ID is required");
        }
        if (vehicleDocumentDTO.getDocumentType() == null) {
            throw new ValidationException("Document type is required");
        }
        if (vehicleDocumentDTO.getDocumentNumber() == null || vehicleDocumentDTO.getDocumentNumber().isBlank()) {
            throw new ValidationException("Document number is required");
        }
        if (vehicleDocumentDTO.getIssueDate() == null) {
            throw new ValidationException("Issue date is required");
        }
        if (vehicleDocumentDTO.getExpiryDate() == null) {
            throw new ValidationException("Expiry date is required");
        }
        if (vehicleDocumentDTO.getExpiryDate().isBefore(vehicleDocumentDTO.getIssueDate())) {
            throw new ValidationException("Expiry date cannot be before issue date");
        }
        if (vehicleDocumentDTO.getStatus() == null) {
            throw new ValidationException("Document status is required");
        }

        String documentNumber = vehicleDocumentDTO.getDocumentNumber().trim();

        if (vehicleDocumentRepository.existsByDocumentNumber(documentNumber)) {
            throw new DuplicateException("Document number already exists");
        }

        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleDocumentDTO.getVehicleId());
        if (optionalVehicle.isEmpty()) {
            throw new NotFoundException("Vehicle not found");
        }

        Vehicle vehicle = optionalVehicle.get();

        VehicleDocument vehicleDocument = new VehicleDocument();
        vehicleDocument.setVehicle(vehicle);
        vehicleDocument.setDocumentType(vehicleDocumentDTO.getDocumentType());
        vehicleDocument.setDocumentNumber(documentNumber);
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
        if (documentId == null) {
            throw new ValidationException("Document ID is required");
        }

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
        if (vehicleDocumentDTO == null) {
            throw new ValidationException("Vehicle document data is required");
        }
        if (vehicleDocumentDTO.getDocumentId() == null) {
            throw new ValidationException("Document ID is required");
        }
        if (vehicleDocumentDTO.getVehicleId() == null) {
            throw new ValidationException("Vehicle ID is required");
        }
        if (vehicleDocumentDTO.getDocumentType() == null) {
            throw new ValidationException("Document type is required");
        }
        if (vehicleDocumentDTO.getDocumentNumber() == null || vehicleDocumentDTO.getDocumentNumber().isBlank()) {
            throw new ValidationException("Document number is required");
        }
        if (vehicleDocumentDTO.getIssueDate() == null) {
            throw new ValidationException("Issue date is required");
        }
        if (vehicleDocumentDTO.getExpiryDate() == null) {
            throw new ValidationException("Expiry date is required");
        }
        if (vehicleDocumentDTO.getExpiryDate().isBefore(vehicleDocumentDTO.getIssueDate())) {
            throw new ValidationException("Expiry date cannot be before issue date");
        }
        if (vehicleDocumentDTO.getStatus() == null) {
            throw new ValidationException("Document status is required");
        }

        Optional<VehicleDocument> optionalVehicleDocument = vehicleDocumentRepository.findById(vehicleDocumentDTO.getDocumentId());
        if (optionalVehicleDocument.isEmpty()) {
            throw new NotFoundException("Vehicle document not found");
        }

        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleDocumentDTO.getVehicleId());
        if (optionalVehicle.isEmpty()) {
            throw new NotFoundException("Vehicle not found");
        }

        String documentNumber = vehicleDocumentDTO.getDocumentNumber().trim();

        if (vehicleDocumentRepository.existsByDocumentNumberAndDocumentIdNot(documentNumber, vehicleDocumentDTO.getDocumentId())) {
            throw new DuplicateException("Document number already exists");
        }

        VehicleDocument vehicleDocument = optionalVehicleDocument.get();
        vehicleDocument.setVehicle(optionalVehicle.get());
        vehicleDocument.setDocumentType(vehicleDocumentDTO.getDocumentType());
        vehicleDocument.setDocumentNumber(documentNumber);
        vehicleDocument.setIssueDate(vehicleDocumentDTO.getIssueDate());
        vehicleDocument.setExpiryDate(vehicleDocumentDTO.getExpiryDate());
        vehicleDocument.setStatus(vehicleDocumentDTO.getStatus());

        vehicleDocumentRepository.save(vehicleDocument);
    }

    @Override
    @Transactional
    public void deleteVehicleDocument(Long documentId) {
        if (documentId == null) {
            throw new ValidationException("Document ID is required");
        }

        Optional<VehicleDocument> optionalVehicleDocument = vehicleDocumentRepository.findById(documentId);
        if (optionalVehicleDocument.isEmpty()) {
            throw new NotFoundException("Vehicle document not found");
        }

        vehicleDocumentRepository.deleteById(documentId);
    }
}
