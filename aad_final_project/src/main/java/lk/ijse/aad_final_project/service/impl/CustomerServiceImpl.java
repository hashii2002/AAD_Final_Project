package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.CustomerDTO;
import lk.ijse.aad_final_project.entity.Customer;
import lk.ijse.aad_final_project.entity.User;
import lk.ijse.aad_final_project.repository.CustomerRepository;
import lk.ijse.aad_final_project.repository.UserRepository;
import lk.ijse.aad_final_project.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;


    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        Optional<User> optionalUser =
                userRepository.findById(customerDTO.getUserId());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        Customer customer = new Customer();

        customer.setNic(customerDTO.getNic());
        customer.setAddress(customerDTO.getAddress());
        customer.setDrivingLicenseNumber(
                customerDTO.getDrivingLicenseNumber()
        );
        customer.setUser(user);

        customerRepository.save(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerDTO> customerDTOList = new ArrayList<>();

        for (Customer customer : customers) {

            CustomerDTO customerDTO = new CustomerDTO();

            customerDTO.setCustomerId(customer.getCustomerId());
            customerDTO.setUserId(customer.getUser().getUserId());
            customerDTO.setNic(customer.getNic());
            customerDTO.setAddress(customer.getAddress());
            customerDTO.setDrivingLicenseNumber(
                    customer.getDrivingLicenseNumber()
            );

            customerDTOList.add(customerDTO);
        }

        return customerDTOList;
    }

    @Override
    public CustomerDTO selectCustomer(Long customerId) {
        Optional<Customer> optionalCustomer =
                customerRepository.findById(customerId);

        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Customer customer = optionalCustomer.get();

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setCustomerId(customer.getCustomerId());
        customerDTO.setUserId(customer.getUser().getUserId());
        customerDTO.setNic(customer.getNic());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setDrivingLicenseNumber(
                customer.getDrivingLicenseNumber()
        );

        return customerDTO;
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {

        Optional<Customer> optionalCustomer =
                customerRepository.findById(customerDTO.getCustomerId());

        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Customer customer = optionalCustomer.get();

        customer.setNic(customerDTO.getNic());
        customer.setAddress(customerDTO.getAddress());
        customer.setDrivingLicenseNumber(
                customerDTO.getDrivingLicenseNumber()
        );

        customerRepository.save(customer);

    }

    @Override
    public void deleteCustomer(Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("Customer not found");
        }

        customerRepository.deleteById(customerId);
    }

    @Override
    public CustomerDTO getCustomerByUsername(String username) {
        Optional<Customer> optionalCustomer = customerRepository.findByUser_Username(username);

        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Customer customer = optionalCustomer.get();

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setCustomerId(customer.getCustomerId());
        customerDTO.setUserId(customer.getUser().getUserId());
        customerDTO.setNic(customer.getNic());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setDrivingLicenseNumber(customer.getDrivingLicenseNumber());

        return customerDTO;
    }
}
