package lk.ijse.aad_final_project.controller;

import jakarta.validation.Valid;
import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.RentalRateDTO;
import lk.ijse.aad_final_project.service.RentalRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/rentalRate")
@CrossOrigin
@RequiredArgsConstructor
public class RentalRateController {

    private final RentalRateService rentalRateService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveRentalRate(@Valid @RequestBody RentalRateDTO rentalRateDTO) {
        rentalRateService.saveRentalRate(rentalRateDTO);
        CommonResponse response = new CommonResponse(0, "Rental Rate Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllRentalRates() {
        List<RentalRateDTO> allRentalRates = rentalRateService.getAllRentalRates();
        CommonResponse response = new CommonResponse(0, allRentalRates, "Get All Rental Rates API Successful");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{rateId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectRentalRate(@PathVariable Long rateId) {
        RentalRateDTO rentalRateDTO = rentalRateService.selectRentalRate(rateId);
        CommonResponse response = new CommonResponse(0, rentalRateDTO, "Rental Rate Selected Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/category/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getRentalRateByCategoryId(@PathVariable Long categoryId) {
        RentalRateDTO rentalRateDTO = rentalRateService.getRentalRateByCategoryId(categoryId);
        CommonResponse response = new CommonResponse(0, rentalRateDTO, "Rental Rate Retrieved Successfully for Category");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateRentalRate(@Valid @RequestBody RentalRateDTO rentalRateDTO) {
        rentalRateService.updateRentalRate(rentalRateDTO);
        CommonResponse response = new CommonResponse(0, "Rental Rate Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{rateId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteRentalRate(@PathVariable Long rateId) {
        rentalRateService.deleteRentalRate(rateId);
        CommonResponse response = new CommonResponse(0, "Rental Rate Deleted Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
