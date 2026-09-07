package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.RentalDriverDTO;
import lk.ijse.aad_final_project.service.RentalDriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/rentalDriver")
@CrossOrigin
@RequiredArgsConstructor
public class RentalDriverController {

    private final RentalDriverService rentalDriverService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveRentalDriver(@RequestBody RentalDriverDTO rentalDriverDTO) {
        rentalDriverService.saveRentalDriver(rentalDriverDTO);
        return new CommonResponse(0, "Rental Driver Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllRentalDrivers() {
        List<RentalDriverDTO> rentalDrivers = rentalDriverService.getAllRentalDrivers();
        return new CommonResponse(0, rentalDrivers, "Get All Rental Drivers Successful");
    }

    @GetMapping(value = "/select/{rentalDriverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectRentalDriver(@PathVariable Long rentalDriverId) {
        RentalDriverDTO rentalDriverDTO = rentalDriverService.selectRentalDriver(rentalDriverId);
        return new CommonResponse(0, rentalDriverDTO, "Rental Driver Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateRentalDriver(@RequestBody RentalDriverDTO rentalDriverDTO) {
        rentalDriverService.updateRentalDriver(rentalDriverDTO);
        return new CommonResponse(0, "Rental Driver Updated Successfully");
    }

    @DeleteMapping(value = "/{rentalDriverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteRentalDriver(@PathVariable Long rentalDriverId) {
        rentalDriverService.deleteRentalDriver(rentalDriverId);
        return new CommonResponse(0, "Rental Driver Deleted Successfully");
    }

}
