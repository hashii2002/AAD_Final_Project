package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.CustomerDTO;
import lk.ijse.aad_final_project.dto.CustomerRegisterDTO;
import lk.ijse.aad_final_project.entity.Customer;
import lk.ijse.aad_final_project.entity.Role;
import lk.ijse.aad_final_project.entity.User;
import lk.ijse.aad_final_project.enums.RoleName;
import lk.ijse.aad_final_project.enums.UserStatus;
import lk.ijse.aad_final_project.exception.DuplicateException;
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

        if (customerDTO == null) {
            throw new ValidationException("Customer data is required");
        }

        if (customerDTO.getUserId() == null) {
            throw new ValidationException("User ID is required");
        }

        if (customerDTO.getNic() == null || customerDTO.getNic().isBlank()) {
            throw new ValidationException("NIC is required");
        }

        if (customerDTO.getAddress() == null || customerDTO.getAddress().isBlank()) {
            throw new ValidationException("Address is required");
        }

        if (customerDTO.getDrivingLicenseNumber() == null || customerDTO.getDrivingLicenseNumber().isBlank()) {
            throw new ValidationException("Driving license number is required");
        }

        String nic = customerDTO.getNic().trim().toUpperCase();
        String address = customerDTO.getAddress().trim();
        String drivingLicenseNumber = customerDTO.getDrivingLicenseNumber().trim().toUpperCase();

        User user = userRepository.findById(customerDTO.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ValidationException("Inactive user cannot be registered as a customer");
        }

        if (user.getRole() == null || user.getRole().getRoleName() == null) {
            throw new ValidationException("User role is not configured");
        }

        if (user.getRole().getRoleName() != RoleName.CUSTOMER) {
            throw new ValidationException("Selected user does not have CUSTOMER role");
        }

        if (customerRepository.existsByUserId(user.getUserId())) {
            throw new DuplicateException("Customer profile already exists for this user");
        }

        if (customerRepository.existsByNic(nic)) {
            throw new DuplicateException("NIC already exists");
        }

        if (customerRepository.existsByDrivingLicenseNumber(drivingLicenseNumber)) {
            throw new DuplicateException("Driving license number already exists");
        }

        Customer customer = new Customer();

        customer.setNic(nic);
        customer.setAddress(address);
        customer.setDrivingLicenseNumber(drivingLicenseNumber);
        customer.setUser(user);

        customerRepository.save(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOList = new ArrayList<>();

        for (Customer customer : customers) {
            CustomerDTO customerDTO = convertToDTO(customer);
            customerDTOList.add(customerDTO);
        }

        return customerDTOList;
    }

    @Override
    public CustomerDTO selectCustomer(Long customerId) {
        if (customerId == null) {
            throw new ValidationException("Customer ID is required");
        }

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer not found"));

        return convertToDTO(customer);
    }

    @Override
    @Transactional
    public void updateCustomer(CustomerDTO customerDTO, String username) {

        if (customerDTO == null) {
            throw new ValidationException("Customer data is required");
        }

        if (username == null || username.isBlank()) {
            throw new ValidationException("Authenticated user is required");
        }

        if (customerDTO.getNic() == null || customerDTO.getNic().isBlank()) {
            throw new ValidationException("NIC is required");
        }

        if (customerDTO.getAddress() == null || customerDTO.getAddress().isBlank()) {
            throw new ValidationException("Address is required");
        }

        if (customerDTO.getDrivingLicenseNumber() == null || customerDTO.getDrivingLicenseNumber().isBlank()) {
            throw new ValidationException("Driving license number is required");
        }

        String nic = customerDTO.getNic().trim().toUpperCase();
        String address = customerDTO.getAddress().trim();
        String drivingLicenseNumber = customerDTO.getDrivingLicenseNumber().trim().toUpperCase();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getRole() == null || user.getRole().getRoleName() == null) {
            throw new ValidationException("User role is not configured");
        }

        Customer customer;

        if (user.getRole().getRoleName() == RoleName.CUSTOMER) {
            customer = customerRepository.findCustomerByUsername(username).orElseThrow(() -> new NotFoundException("Customer profile not found"));

            if (customerDTO.getUserId() != null && !customer.getUser().getUserId().equals(customerDTO.getUserId())) {
                throw new ValidationException("You cannot change the user associated with your profile");
            }
        }
        else {
            if (customerDTO.getCustomerId() == null) {
                throw new ValidationException("Customer ID is required");
            }
            customer = customerRepository.findById(customerDTO.getCustomerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        }

        if (customerRepository.existsByNicAndCustomerIdNot(nic, customer.getCustomerId())) {
            throw new DuplicateException("NIC already exists");
        }

        if (customerRepository.existsByDrivingLicenseNumberAndCustomerIdNot(drivingLicenseNumber, customer.getCustomerId())) {
            throw new DuplicateException("Driving license number already exists");
        }

        customer.setNic(nic);
        customer.setAddress(address);
        customer.setDrivingLicenseNumber(drivingLicenseNumber);

        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void patchCustomer(CustomerDTO customerDTO, String username) {

        if (customerDTO == null) {
            throw new ValidationException("Customer data is required");
        }

        if (username == null || username.isBlank()) {
            throw new ValidationException("Authenticated user is required");
        }

        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getRole() == null || user.getRole().getRoleName() == null) {
            throw new ValidationException("User role is not configured");
        }
        Customer customer;

        if (user.getRole().getRoleName() == RoleName.CUSTOMER) {
            customer = customerRepository.findCustomerByUsername(username).orElseThrow(() -> new NotFoundException("Customer profile not found"));
        } else {
            if (customerDTO.getCustomerId() == null) {
                throw new ValidationException("Customer ID is required");
            }
            customer = customerRepository.findById(customerDTO.getCustomerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        }

        if (customerDTO.getUserId() != null && !customer.getUser().getUserId().equals(customerDTO.getUserId())) {
            throw new ValidationException("User ID cannot be changed");
        }
        if (customerDTO.getNic() != null) {
            if (customerDTO.getNic().isBlank()) {
                throw new ValidationException("NIC cannot be empty");
            }

            String nic = customerDTO.getNic().trim().toUpperCase();

            if (customerRepository.existsByNicAndCustomerIdNot(nic, customer.getCustomerId())) {
                throw new DuplicateException("NIC already exists");
            }

            customer.setNic(nic);
        }

        if (customerDTO.getAddress() != null) {
            if (customerDTO.getAddress().isBlank()) {
                throw new ValidationException("Address cannot be empty");
            }
            String address = customerDTO.getAddress().trim();

            customer.setAddress(address);
        }

        if (customerDTO.getDrivingLicenseNumber() != null) {
            if (customerDTO.getDrivingLicenseNumber().isBlank()) {
                throw new ValidationException("Driving license number cannot be empty");
            }

            String drivingLicenseNumber = customerDTO.getDrivingLicenseNumber().trim().toUpperCase();

            if (customerRepository.existsByDrivingLicenseNumberAndCustomerIdNot(drivingLicenseNumber, customer.getCustomerId())) {
                throw new DuplicateException("Driving license number already exists");
            }
            customer.setDrivingLicenseNumber(drivingLicenseNumber);
        }
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long customerId) {

        if (customerId == null) {
            throw new ValidationException("Customer ID is required");
        }

        if (!customerRepository.existsById(customerId)) {
            throw new NotFoundException("Customer not found");
        }
        customerRepository.deleteById(customerId);
    }

    @Override
    public CustomerDTO getCustomerByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }

        Customer customer = customerRepository.findCustomerByUsername(username).orElseThrow(() -> new NotFoundException("Customer not found"));
        return convertToDTO(customer);
    }

    @Override
    @Transactional
    public void registerCustomer(CustomerRegisterDTO dto) {
        if (dto == null) {
            throw new ValidationException("Registration data is required");
        }
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new ValidationException("Username is required");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new ValidationException("Email is required");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new ValidationException("Password is required");
        }
        if (dto.getFirstName() == null || dto.getFirstName().isBlank()) {
            throw new ValidationException("First name is required");
        }
        if (dto.getLastName() == null || dto.getLastName().isBlank()) {
            throw new ValidationException("Last name is required");
        }
        if (dto.getPhone() == null || dto.getPhone().isBlank()) {
            throw new ValidationException("Phone is required");
        }
        if (dto.getNic() == null || dto.getNic().isBlank()) {
            throw new ValidationException("NIC is required");
        }
        if (dto.getAddress() == null || dto.getAddress().isBlank()) {
            throw new ValidationException("Address is required");
        }

        if (dto.getDrivingLicenseNumber() == null || dto.getDrivingLicenseNumber().isBlank()) {
            throw new ValidationException("Driving license number is required");
        }

        String username = dto.getUsername().trim();
        String email = dto.getEmail().trim().toLowerCase();
        String firstName = dto.getFirstName().trim();
        String lastName = dto.getLastName().trim();
        String phone = dto.getPhone().trim();
        String nic = dto.getNic().trim().toUpperCase();
        String address = dto.getAddress().trim();
        String drivingLicenseNumber = dto.getDrivingLicenseNumber().trim().toUpperCase();

        if (userRepository.existsByUsername(username)) {
            throw new DuplicateException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateException("Email already exists");
        }

        if (customerRepository.existsByNic(nic)) {
            throw new DuplicateException("NIC already exists");
        }

        if (customerRepository.existsByDrivingLicenseNumber(drivingLicenseNumber)) {
            throw new DuplicateException("Driving license number already exists");
        }

        Role customerRole = roleRepository.findRoleByRoleName(RoleName.CUSTOMER).orElseThrow(() -> new NotFoundException("Default CUSTOMER role not found in system"));

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(customerRole);
        User savedUser = userRepository.save(user);

        Customer customer = new Customer();

        customer.setNic(nic);
        customer.setAddress(address);
        customer.setDrivingLicenseNumber(drivingLicenseNumber);
        customer.setUser(savedUser);

        customerRepository.save(customer);
    }

    private CustomerDTO convertToDTO(Customer customer) {

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customer.getCustomerId());

        if (customer.getUser() != null) {
            customerDTO.setUserId(customer.getUser().getUserId());
        }

        customerDTO.setNic(customer.getNic());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setDrivingLicenseNumber(customer.getDrivingLicenseNumber());

        return customerDTO;
    }
}