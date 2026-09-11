package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.CustomerDTO;
import lk.ijse.aad_final_project.dto.CustomerRegisterDTO;
import lk.ijse.aad_final_project.entity.Customer;
import lk.ijse.aad_final_project.entity.Role;
import lk.ijse.aad_final_project.entity.User;
import lk.ijse.aad_final_project.enums.UserStatus;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
import lk.ijse.aad_final_project.repository.CustomerRepository;
import lk.ijse.aad_final_project.repository.RoleRepository;
import lk.ijse.aad_final_project.repository.UserRepository;
import lk.ijse.aad_final_project.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void saveCustomer(CustomerDTO customerDTO) {
        Optional<User> optionalUser = userRepository.findById(customerDTO.getUserId());

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User user = optionalUser.get();

        Customer customer = new Customer();

        customer.setNic(customerDTO.getNic());
        customer.setAddress(customerDTO.getAddress());
        customer.setDrivingLicenseNumber(customerDTO.getDrivingLicenseNumber());
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
            customerDTO.setDrivingLicenseNumber(customer.getDrivingLicenseNumber());

            customerDTOList.add(customerDTO);
        }

        return customerDTOList;
    }

    @Override
    public CustomerDTO selectCustomer(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("Customer not found");
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

    @Override
    @Transactional
    public void updateCustomer(CustomerDTO customerDTO, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));

        Customer customer;
        if ("CUSTOMER".equals(user.getRole().getRoleName())) {
            customer = customerRepository.findByUser_Username(username).orElseThrow(() -> new NotFoundException("Customer profile not found"));

            if (customerDTO.getUserId() != null && !customer.getUser().getUserId().equals(customerDTO.getUserId())) {
                throw new ValidationException("You cannot change the user associated with your profile");
            }
        } else {
            if (customerDTO.getCustomerId() == null) {
                throw new ValidationException("Customer ID is required");
            }
            customer = customerRepository.findById(customerDTO.getCustomerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        }

        customer.setNic(customerDTO.getNic());
        customer.setAddress(customerDTO.getAddress());
        customer.setDrivingLicenseNumber(customerDTO.getDrivingLicenseNumber());

        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void patchCustomer(CustomerDTO customerDTO, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));

        Customer customer;
        if ("CUSTOMER".equals(user.getRole().getRoleName())) {
            customer = customerRepository.findByUser_Username(username).orElseThrow(() -> new NotFoundException("Customer profile not found"));
        } else {
            if (customerDTO.getCustomerId() == null) {
                throw new ValidationException("Customer ID is required");
            }
            customer = customerRepository.findById(customerDTO.getCustomerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        }

        if (customerDTO.getNic() != null) {
            if (customerDTO.getNic().isBlank()) {
                throw new ValidationException("NIC cannot be empty");
            }
            customer.setNic(customerDTO.getNic());
        }

        if (customerDTO.getAddress() != null) {
            if (customerDTO.getAddress().isBlank()) {
                throw new ValidationException("Address cannot be empty");
            }
            customer.setAddress(customerDTO.getAddress());
        }

        if (customerDTO.getDrivingLicenseNumber() != null) {
            if (customerDTO.getDrivingLicenseNumber().isBlank()) {
                throw new ValidationException("Driving license number cannot be empty");
            }
            customer.setDrivingLicenseNumber(customerDTO.getDrivingLicenseNumber());
        }

        if (customerDTO.getUserId() != null && !customer.getUser().getUserId().equals(customerDTO.getUserId())) {
            throw new ValidationException("User ID cannot be changed");
        }
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new NotFoundException("Customer not found");
        }
        customerRepository.deleteById(customerId);
    }

    @Override
    public CustomerDTO getCustomerByUsername(String username) {
        Optional<Customer> optionalCustomer = customerRepository.findByUser_Username(username);

        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("Customer not found");
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

    @Override
    @Transactional
    public void registerCustomer(CustomerRegisterDTO dto) {
        Role customerRole = roleRepository.findAll().stream()
                .filter(r -> r.getRoleName().name().equals("CUSTOMER"))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Default CUSTOMER role not found in system"));

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(customerRole);

        User savedUser = userRepository.save(user);

        Customer customer = new Customer();
        customer.setNic(dto.getNic());
        customer.setAddress(dto.getAddress());
        customer.setDrivingLicenseNumber(dto.getDrivingLicenseNumber());
        customer.setUser(savedUser);

        customerRepository.save(customer);
    }
}
