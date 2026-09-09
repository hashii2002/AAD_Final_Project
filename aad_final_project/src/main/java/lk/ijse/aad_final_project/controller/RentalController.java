package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.RentalDTO;
import lk.ijse.aad_final_project.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/rental")
@CrossOrigin
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveRental(@RequestBody RentalDTO rentalDTO) {
        rentalService.saveRental(rentalDTO);
        CommonResponse response = new CommonResponse(0, "Rental Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllRentals() {
        List<RentalDTO> allRentals = rentalService.getAllRentals();
        CommonResponse response = new CommonResponse(0, allRentals, "Get All Rentals API Successful");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{rentalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectRental(@PathVariable Long rentalId) {
        RentalDTO rentalDTO = rentalService.selectRental(rentalId);
        CommonResponse response = new CommonResponse(0, rentalDTO, "Rental Selected Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateRental(@RequestBody RentalDTO rentalDTO) {
        rentalService.updateRental(rentalDTO);
        CommonResponse response = new CommonResponse(0, "Rental Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{rentalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteRental(@PathVariable Long rentalId) {
        rentalService.deleteRental(rentalId);
        CommonResponse response = new CommonResponse(0, "Rental Deleted Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getMyRentals(Authentication authentication) {
        String username = authentication.getName();
        List<RentalDTO> rentalDTOList = rentalService.getMyRentals(username);
        CommonResponse response = new CommonResponse(0, rentalDTOList, "My Rentals Retrieved Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
