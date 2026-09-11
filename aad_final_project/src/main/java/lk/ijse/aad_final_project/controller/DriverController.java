package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.DriverDTO;
import lk.ijse.aad_final_project.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/v1/driver")
@RequiredArgsConstructor
@CrossOrigin
public class DriverController {

    private final DriverService driverService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveDriver( @Valid @RequestBody DriverDTO driverDTO) {
        driverService.saveDriver(driverDTO);
        CommonResponse response = new CommonResponse(0, "Driver Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllDrivers() {
        List<DriverDTO> allDrivers = driverService.getAllDrivers();
        CommonResponse response = new CommonResponse(0, allDrivers, "Get All Drivers API Successful");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectDriver(@PathVariable Long driverId) {
        DriverDTO driverDTO = driverService.selectDriver(driverId);
        CommonResponse response = new CommonResponse(0, driverDTO, "Driver Selected Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateDriver( @Valid @RequestBody DriverDTO driverDTO) {
        driverService.updateDriver(driverDTO);
        CommonResponse response = new CommonResponse(0, "Driver Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getMyProfile(Authentication authentication) {
        String username = authentication.getName();
        DriverDTO driverDTO = driverService.getDriverByUsername(username);
        CommonResponse response = new CommonResponse(0, driverDTO, "Driver Profile Retrieved Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteDriver(@PathVariable Long driverId) {
        driverService.deleteDriver(driverId);
        CommonResponse response = new CommonResponse(0, "Driver Deleted Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
