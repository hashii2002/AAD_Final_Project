package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.RentalRateDTO;
import lk.ijse.aad_final_project.service.RentalRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/rentalRate")
@CrossOrigin
@RequiredArgsConstructor
public class RentalRateController {

    private final RentalRateService rentalRateService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveRentalRate(@RequestBody RentalRateDTO rentalRateDTO) {

        rentalRateService.saveRentalRate(rentalRateDTO);

        return new CommonResponse(0, "Rental Rate Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllRentalRates() {

        List<RentalRateDTO> allRentalRates = rentalRateService.getAllRentalRates();

        return new CommonResponse(0, allRentalRates, "Get All Rental Rates API Successful");
    }

    @GetMapping(value = "/select/{rateId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectRentalRate(@PathVariable Long rateId) {

        RentalRateDTO rentalRateDTO = rentalRateService.selectRentalRate(rateId);

        return new CommonResponse(0, rentalRateDTO, "Rental Rate Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateRentalRate(@RequestBody RentalRateDTO rentalRateDTO) {

        rentalRateService.updateRentalRate(rentalRateDTO);

        return new CommonResponse(0, "Rental Rate Updated Successfully");
    }

    @DeleteMapping(value = "/{rateId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteRentalRate(@PathVariable Long rateId) {

        rentalRateService.deleteRentalRate(rateId);

        return new CommonResponse(0, "Rental Rate Deleted Successfully");
    }

}
