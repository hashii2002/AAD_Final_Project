package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.VehicleBrandDTO;
import lk.ijse.aad_final_project.service.VehicleBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/brand")
@RequiredArgsConstructor
@CrossOrigin
public class VehicleBrandController {

    private final VehicleBrandService vehicleBrandService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveVehicleBrand(@RequestBody VehicleBrandDTO vehicleBrandDTO) {
        vehicleBrandService.saveVehicleBrand(vehicleBrandDTO);
        return new CommonResponse(0, "Vehicle Brand Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllVehicleBrands() {
        List<VehicleBrandDTO> allVehicleBrands = vehicleBrandService.getAllVehicleBrands();
        return new CommonResponse(0, allVehicleBrands, "Get All Vehicle Brands API Successful");
    }

    @GetMapping(value = "/select/{brandId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectVehicleBrand(@PathVariable Long brandId) {
        VehicleBrandDTO vehicleBrandDTO = vehicleBrandService.selectVehicleBrand(brandId);
        return new CommonResponse(0, vehicleBrandDTO, "Vehicle Brand Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateVehicleBrand(@RequestBody VehicleBrandDTO vehicleBrandDTO) {
        vehicleBrandService.updateVehicleBrand(vehicleBrandDTO);
        return new CommonResponse(0, "Vehicle Brand Updated Successfully");
    }

    @DeleteMapping(value = "/{brandId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteVehicleBrand(@PathVariable Long brandId) {
        vehicleBrandService.deleteVehicleBrand(brandId);
        return new CommonResponse(0, "Vehicle Brand Deleted Successfully");
    }
}
