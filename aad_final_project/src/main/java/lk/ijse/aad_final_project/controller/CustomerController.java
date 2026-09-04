package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.CustomerDTO;
import lk.ijse.aad_final_project.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/customer")
@RequiredArgsConstructor
@CrossOrigin
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveCustomer(@RequestBody CustomerDTO customerDTO) {

        customerService.saveCustomer(customerDTO);

        return new CommonResponse(0, "Customer Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllCustomers() {

        List<CustomerDTO> allCustomers = customerService.getAllCustomers();

        return new CommonResponse(0, allCustomers, "Get All Customers API Successful");
    }

    @GetMapping(value = "/select/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectCustomer(@PathVariable Long customerId) {

        CustomerDTO customerDTO = customerService.selectCustomer(customerId);

        return new CommonResponse(0, customerDTO, "Customer Selected Successfully"
        );
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getMyProfile(@RequestParam Long userId) {

        CustomerDTO customerDTO = customerService.getCustomerByUserId(userId);

        return new CommonResponse(0, customerDTO, "Customer Profile Retrieved Successfully"
        );
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateCustomer(@RequestBody CustomerDTO customerDTO) {

        customerService.updateCustomer(customerDTO);

        return new CommonResponse(0, "Customer Updated Successfully"
        );
    }

    @DeleteMapping(value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteCustomer(@PathVariable Long customerId) {

        customerService.deleteCustomer(customerId);

        return new CommonResponse(0, "Customer Deleted Successfully"
        );
    }


}
