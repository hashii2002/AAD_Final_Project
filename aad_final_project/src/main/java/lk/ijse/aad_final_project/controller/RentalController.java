package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.RentalDTO;
import lk.ijse.aad_final_project.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    public CommonResponse saveRental(@RequestBody RentalDTO rentalDTO) {

        rentalService.saveRental(rentalDTO);

        return new CommonResponse(0, "Rental Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllRentals() {

        List<RentalDTO> allRentals = rentalService.getAllRentals();

        return new CommonResponse(0, allRentals, "Get All Rentals API Successful");
    }

    @GetMapping(value = "/select/{rentalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectRental(@PathVariable Long rentalId) {

        RentalDTO rentalDTO = rentalService.selectRental(rentalId);

        return new CommonResponse(0, rentalDTO, "Rental Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateRental(@RequestBody RentalDTO rentalDTO) {

        rentalService.updateRental(rentalDTO);

        return new CommonResponse(0, "Rental Updated Successfully");
    }

    @DeleteMapping(value = "/{rentalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteRental(@PathVariable Long rentalId) {

        rentalService.deleteRental(rentalId);

        return new CommonResponse(0, "Rental Deleted Successfully");
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getMyRentals(Authentication authentication) {

        String username = authentication.getName();

        List<RentalDTO> rentalDTOList = rentalService.getMyRentals(username);

        return new CommonResponse(0, rentalDTOList, "My Rentals Retrieved Successfully");
    }
}
