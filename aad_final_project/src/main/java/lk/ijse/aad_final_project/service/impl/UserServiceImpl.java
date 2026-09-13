package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.UserDTO;
import lk.ijse.aad_final_project.entity.Role;
import lk.ijse.aad_final_project.entity.User;
import lk.ijse.aad_final_project.enums.UserStatus;
import lk.ijse.aad_final_project.exception.DuplicateException;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
import lk.ijse.aad_final_project.repository.RoleRepository;
import lk.ijse.aad_final_project.repository.UserRepository;
import lk.ijse.aad_final_project.service.UserService;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void saveUser(UserDTO userDTO) {

        if (userDTO == null) {
            throw new ValidationException("User data is required");
        }
        if (userDTO.getRoleId() == null) {
            throw new ValidationException("Role ID is required");
        }
        if (userDTO.getUsername() == null || userDTO.getUsername().isBlank()) {
            throw new ValidationException("Username is required");
        }
        if (userDTO.getEmail() == null || userDTO.getEmail().isBlank()) {
            throw new ValidationException("Email is required");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
            throw new ValidationException("Password is required");
        }
        if (userDTO.getFirstName() == null || userDTO.getFirstName().isBlank()) {
            throw new ValidationException("First name is required");
        }
        if (userDTO.getLastName() == null || userDTO.getLastName().isBlank()) {
            throw new ValidationException("Last name is required");
        }
        if (userDTO.getPhone() == null || userDTO.getPhone().isBlank()) {
            throw new ValidationException("Phone number is required");
        }
        if (userDTO.getStatus() == null) {
            throw new ValidationException("User status is required");
        }

        String username = userDTO.getUsername().trim();
        String email = userDTO.getEmail().trim().toLowerCase();
        String firstName = userDTO.getFirstName().trim();
        String lastName = userDTO.getLastName().trim();
        String phone = userDTO.getPhone().trim();

        Optional<Role> optionalRole = roleRepository.findById(userDTO.getRoleId());
        if (optionalRole.isEmpty()) {
            throw new NotFoundException("Role not found");
        }

        if (userRepository.existsByUsername(username)) {
            throw new DuplicateException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateException("Email already exists");
        }

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setStatus(userDTO.getStatus());
        user.setRole(optionalRole.get());

        userRepository.save(user);

    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserDTO> userDTOList = new ArrayList<>();

        for (User user : users) {

            UserDTO userDTO = new UserDTO();

            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(null);
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setPhone(user.getPhone());
            userDTO.setStatus(user.getStatus());
            if (user.getRole() != null) {
                userDTO.setRoleId(user.getRole().getRoleId());
            }
            if (user.getCustomer() != null) {
                userDTO.setCustomerId(user.getCustomer().getCustomerId());
            }
            if (user.getDriver() != null) {
                userDTO.setDriverId(user.getDriver().getDriverId());
            }

            userDTOList.add(userDTO);
        }

        return userDTOList;
    }

    @Override
    public UserDTO selectUser(Long userId) {
        if (userId == null) {
            throw new ValidationException("User ID is required");
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User user = optionalUser.get();

        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(null);
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhone(user.getPhone());
        userDTO.setStatus(user.getStatus());

        if (user.getRole() != null) {
            userDTO.setRoleId(user.getRole().getRoleId());
        }
        if (user.getCustomer() != null) {
            userDTO.setCustomerId(user.getCustomer().getCustomerId());
        }
        if (user.getDriver() != null) {
            userDTO.setDriverId(user.getDriver().getDriverId());
        }
        return userDTO;
    }

    @Override
    @Transactional
    public void updateUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new ValidationException("User data is required");
        }
        if (userDTO.getUserId() == null) {
            throw new ValidationException("User ID is required");
        }
        if (userDTO.getRoleId() == null) {
            throw new ValidationException("Role ID is required");
        }
        if (userDTO.getUsername() == null || userDTO.getUsername().isBlank()) {
            throw new ValidationException("Username is required");
        }

        if (userDTO.getEmail() == null || userDTO.getEmail().isBlank()) {
            throw new ValidationException("Email is required");
        }

        if (userDTO.getFirstName() == null || userDTO.getFirstName().isBlank()) {
            throw new ValidationException("First name is required");
        }
        if (userDTO.getPhone() == null || userDTO.getPhone().isBlank()) {
            throw new ValidationException("Phone number is required");
        }

        if (userDTO.getStatus() == null) {
            throw new ValidationException("User status is required");
        }

        Optional<User> optionalUser = userRepository.findById(userDTO.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User user = optionalUser.get();

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new ValidationException("Inactive user cannot be updated");
        }

        String username = userDTO.getUsername().trim();
        String email = userDTO.getEmail().trim().toLowerCase();
        String firstName = userDTO.getFirstName().trim();
        String lastName = userDTO.getLastName().trim();
        String phone = userDTO.getPhone().trim();

        if (userRepository.existsByUsernameAndUserIdNot(username, userDTO.getUserId())) {
            throw new DuplicateException("Username already exists");
        }
        if (userRepository.existsByEmailAndUserIdNot(email, userDTO.getUserId())) {
            throw new DuplicateException("Email already exists");
        }

        Optional<Role> optionalRole = roleRepository.findById(userDTO.getRoleId());
        if (optionalRole.isEmpty()) {
            throw new NotFoundException("Role not found");}

        Role role = optionalRole.get();

        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setStatus(userDTO.getStatus());
        user.setRole(role);

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(user);

    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new ValidationException("User ID is required");
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User user = optionalUser.get();

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new ValidationException("User is already inactive");
        }
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    @Override
    public UserDTO getUserDetails(String username, String password) {

        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }

        if (password == null || password.isBlank()) {
            throw new ValidationException("Password is required");
        }
        username = username.trim();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Invalid username or password");
        }

        User user = optionalUser.get();

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ValidationException("User account is inactive");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new NotFoundException("Invalid username or password");
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(null);
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhone(user.getPhone());
        userDTO.setStatus(user.getStatus());

        if (user.getRole() != null) {
            userDTO.setRoleId(user.getRole().getRoleId());
        }

        if (user.getCustomer() != null) {
            userDTO.setCustomerId(user.getCustomer().getCustomerId());
        }

        if (user.getDriver() != null) {
            userDTO.setDriverId(user.getDriver().getDriverId());
        }

        return userDTO;
    }
}
