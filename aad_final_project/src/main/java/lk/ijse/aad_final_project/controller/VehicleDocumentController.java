package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.VehicleDocumentDTO;
import lk.ijse.aad_final_project.service.VehicleDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/vehicleDocument")
@CrossOrigin
@RequiredArgsConstructor
public class VehicleDocumentController {

    private final VehicleDocumentService vehicleDocumentService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveVehicleDocument(@RequestBody VehicleDocumentDTO vehicleDocumentDTO) {

        vehicleDocumentService.saveVehicleDocument(vehicleDocumentDTO);

        return new CommonResponse(0, "Vehicle Document Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllVehicleDocuments() {

        List<VehicleDocumentDTO> allVehicleDocuments = vehicleDocumentService.getAllVehicleDocuments();

        return new CommonResponse(0, allVehicleDocuments, "Get All Vehicle Documents API Successful");
    }

    @GetMapping(value = "/select/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectVehicleDocument(@PathVariable Long documentId) {

        VehicleDocumentDTO vehicleDocumentDTO = vehicleDocumentService.selectVehicleDocument(documentId);

        return new CommonResponse(0, vehicleDocumentDTO, "Vehicle Document Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateVehicleDocument(@RequestBody VehicleDocumentDTO vehicleDocumentDTO) {

        vehicleDocumentService.updateVehicleDocument(vehicleDocumentDTO);

        return new CommonResponse(0, "Vehicle Document Updated Successfully");
    }

    @DeleteMapping(value = "/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteVehicleDocument(@PathVariable Long documentId) {

        vehicleDocumentService.deleteVehicleDocument(documentId);

        return new CommonResponse(0, "Vehicle Document Deleted Successfully");
    }
}
