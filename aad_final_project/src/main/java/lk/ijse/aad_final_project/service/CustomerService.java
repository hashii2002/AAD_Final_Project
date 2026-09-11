package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {

    void saveCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> getAllCustomers();

    CustomerDTO selectCustomer(Long customerId);

    void updateCustomer(CustomerDTO customerDTO, String username);

    void patchCustomer(CustomerDTO customerDTO, String username);

    void deleteCustomer(Long customerId);

    CustomerDTO getCustomerByUsername(String username);
}
