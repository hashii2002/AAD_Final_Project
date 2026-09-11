package lk.ijse.aad_final_project.controller;

import jakarta.validation.Valid;
import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.CustomerDTO;
import lk.ijse.aad_final_project.dto.CustomerRegisterDTO;
import lk.ijse.aad_final_project.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/customer")
@RequiredArgsConstructor
@CrossOrigin
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        customerService.saveCustomer(customerDTO);
        CommonResponse response = new CommonResponse(0, "Customer Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllCustomers() {
        List<CustomerDTO> allCustomers = customerService.getAllCustomers();
        CommonResponse response = new CommonResponse(0, allCustomers, "Get All Customers API Successful");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectCustomer(@PathVariable Long customerId) {
        CustomerDTO customerDTO = customerService.selectCustomer(customerId);
        CommonResponse response = new CommonResponse(0, customerDTO, "Customer Selected Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getMyProfile(Authentication authentication) {
        String username = authentication.getName();
        CustomerDTO customerDTO = customerService.getCustomerByUsername(username);
        CommonResponse response = new CommonResponse(0, customerDTO, "Customer Profile Retrieved Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateCustomer(@Valid @RequestBody CustomerDTO customerDTO, Authentication authentication) {
        String username = authentication.getName();
        CommonResponse response = new CommonResponse(0, "Customer Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> patchCustomer(@RequestBody CustomerDTO customerDTO, Authentication authentication) {
        String username = authentication.getName();
        customerService.patchCustomer(customerDTO, username);
        CommonResponse response = new CommonResponse(
                0, "Customer Partially Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @DeleteMapping(value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        CommonResponse response = new CommonResponse(0, "Customer Deleted Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> registerCustomer(@Valid @RequestBody CustomerRegisterDTO customerRegisterDTO) {
        customerService.registerCustomer(customerRegisterDTO);
        CommonResponse response = new CommonResponse(0, "Customer Registered Successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
