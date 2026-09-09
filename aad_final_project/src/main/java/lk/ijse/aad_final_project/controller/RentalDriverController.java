package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.RentalDTO;
import lk.ijse.aad_final_project.dto.RentalDriverDTO;
import lk.ijse.aad_final_project.service.RentalDriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/rentalDriver")
@CrossOrigin
@RequiredArgsConstructor
public class RentalDriverController {

    private final RentalDriverService rentalDriverService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveRentalDriver(@RequestBody RentalDriverDTO rentalDriverDTO) {
        rentalDriverService.saveRentalDriver(rentalDriverDTO);
        CommonResponse response = new CommonResponse(0, "Rental Driver Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllRentalDrivers() {
        List<RentalDriverDTO> rentalDrivers = rentalDriverService.getAllRentalDrivers();
        CommonResponse response = new CommonResponse(0, rentalDrivers, "Get All Rental Drivers Successful");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{rentalDriverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectRentalDriver(@PathVariable Long rentalDriverId) {
        RentalDriverDTO rentalDriverDTO = rentalDriverService.selectRentalDriver(rentalDriverId);
        CommonResponse response = new CommonResponse(0, rentalDriverDTO, "Rental Driver Selected Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateRentalDriver(@RequestBody RentalDriverDTO rentalDriverDTO) {
        rentalDriverService.updateRentalDriver(rentalDriverDTO);
        CommonResponse response = new CommonResponse(0, "Rental Driver Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getMyRentals(Authentication authentication) {
        String username = authentication.getName();
        List<RentalDTO> rentalDTOList = rentalDriverService.getMyRentals(username);
        CommonResponse response = new CommonResponse(0, rentalDTOList, "My Assigned Rentals Retrieved Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{rentalDriverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteRentalDriver(@PathVariable Long rentalDriverId) {
        rentalDriverService.deleteRentalDriver(rentalDriverId);
        CommonResponse response = new CommonResponse(0, "Rental Driver Deleted Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
