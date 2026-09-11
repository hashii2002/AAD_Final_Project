package lk.ijse.aad_final_project.controller;

import jakarta.validation.Valid;
import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.VehicleBrandDTO;
import lk.ijse.aad_final_project.service.VehicleBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/brand")
@RequiredArgsConstructor
@CrossOrigin
public class VehicleBrandController {

    private final VehicleBrandService vehicleBrandService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveVehicleBrand(@Valid @RequestBody VehicleBrandDTO vehicleBrandDTO) {
        vehicleBrandService.saveVehicleBrand(vehicleBrandDTO);
        CommonResponse response = new CommonResponse(0, "Vehicle Brand Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllVehicleBrands() {
        List<VehicleBrandDTO> allVehicleBrands = vehicleBrandService.getAllVehicleBrands();
        CommonResponse response = new CommonResponse(0, allVehicleBrands, "Get All Vehicle Brands API Successful");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{brandId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectVehicleBrand(@PathVariable Long brandId) {
        VehicleBrandDTO vehicleBrandDTO = vehicleBrandService.selectVehicleBrand(brandId);
        CommonResponse response = new CommonResponse(0, vehicleBrandDTO, "Vehicle Brand Selected Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateVehicleBrand(@Valid @RequestBody VehicleBrandDTO vehicleBrandDTO) {
        vehicleBrandService.updateVehicleBrand(vehicleBrandDTO);
        CommonResponse response = new CommonResponse(0, "Vehicle Brand Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{brandId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteVehicleBrand(@PathVariable Long brandId) {
        vehicleBrandService.deleteVehicleBrand(brandId);
        CommonResponse response = new CommonResponse(0, "Vehicle Brand Deleted Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
