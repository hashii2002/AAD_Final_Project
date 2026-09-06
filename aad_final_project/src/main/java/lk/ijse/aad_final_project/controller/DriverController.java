package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.DriverDTO;
import lk.ijse.aad_final_project.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/driver")
@RequiredArgsConstructor
@CrossOrigin
public class DriverController {

    private final DriverService driverService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveDriver(@RequestBody DriverDTO driverDTO) {

        driverService.saveDriver(driverDTO);

        return new CommonResponse(0, "Driver Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllDrivers() {

        List<DriverDTO> allDrivers = driverService.getAllDrivers();

        return new CommonResponse(0, allDrivers, "Get All Drivers API Successful");
    }

    @GetMapping(value = "/select/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectDriver(@PathVariable Long driverId) {

        DriverDTO driverDTO = driverService.selectDriver(driverId);

        return new CommonResponse(0, driverDTO, "Driver Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateDriver(@RequestBody DriverDTO driverDTO) {

        driverService.updateDriver(driverDTO);

        return new CommonResponse(0, "Driver Updated Successfully");
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getMyProfile(@RequestParam Long userId) {

        DriverDTO driverDTO = driverService.getDriverByUserId(userId);

        return new CommonResponse(0, driverDTO, "Driver Profile Retrieved Successfully");
    }

    @DeleteMapping(value = "/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteDriver(@PathVariable Long driverId) {

        driverService.deleteDriver(driverId);

        return new CommonResponse(0, "Driver Deleted Successfully");
    }
}
