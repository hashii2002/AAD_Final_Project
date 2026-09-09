package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.VehicleDocumentDTO;
import lk.ijse.aad_final_project.service.VehicleDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/vehicleDocument")
@CrossOrigin
@RequiredArgsConstructor
public class VehicleDocumentController {

    private final VehicleDocumentService vehicleDocumentService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveVehicleDocument(@RequestBody VehicleDocumentDTO vehicleDocumentDTO) {
        vehicleDocumentService.saveVehicleDocument(vehicleDocumentDTO);
        CommonResponse response = new CommonResponse(0, "Vehicle Document Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllVehicleDocuments() {
        List<VehicleDocumentDTO> allVehicleDocuments = vehicleDocumentService.getAllVehicleDocuments();
        CommonResponse response = new CommonResponse(0, allVehicleDocuments, "Get All Vehicle Documents API Successful");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectVehicleDocument(@PathVariable Long documentId) {
        VehicleDocumentDTO vehicleDocumentDTO = vehicleDocumentService.selectVehicleDocument(documentId);
        CommonResponse response = new CommonResponse(0, vehicleDocumentDTO, "Vehicle Document Selected Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateVehicleDocument(@RequestBody VehicleDocumentDTO vehicleDocumentDTO) {
        vehicleDocumentService.updateVehicleDocument(vehicleDocumentDTO);
        CommonResponse response = new CommonResponse(0, "Vehicle Document Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteVehicleDocument(@PathVariable Long documentId) {
        vehicleDocumentService.deleteVehicleDocument(documentId);
        CommonResponse response = new CommonResponse(0, "Vehicle Document Deleted Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}