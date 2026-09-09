package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.UserDTO;
import lk.ijse.aad_final_project.entity.Role;
import lk.ijse.aad_final_project.entity.User;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.repository.RoleRepository;
import lk.ijse.aad_final_project.repository.UserRepository;
import lk.ijse.aad_final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserDTO userDTO) {
        Optional<Role> optionalRole = roleRepository.findById(userDTO.getRoleId());
        if (optionalRole.isEmpty()) {
            throw new NotFoundException("Role not found");
        }

        Role role = optionalRole.get();

        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        user.setStatus(userDTO.getStatus());
        user.setRole(role);

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
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setPhone(user.getPhone());
            userDTO.setStatus(user.getStatus());
            userDTO.setRoleId(user.getRole().getRoleId());

            userDTOList.add(userDTO);
        }

        return userDTOList;
    }

    @Override
    public UserDTO selectUser(Long userId) {
        Optional<User> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User user = optionalUser.get();

        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhone(user.getPhone());
        userDTO.setStatus(user.getStatus());
        userDTO.setRoleId(user.getRole().getRoleId());

        return userDTO;
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userDTO.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        Optional<Role> optionalRole = roleRepository.findById(userDTO.getRoleId());
        if (optionalRole.isEmpty()) {
            throw new NotFoundException("Role not found");
        }

        User user = optionalUser.get();
        Role role = optionalRole.get();

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        user.setStatus(userDTO.getStatus());
        user.setRole(role);

        userRepository.save(user);

    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public UserDTO getUserDetails(String username, String password) {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Invalid username or password");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhone(user.getPhone());
        userDTO.setStatus(user.getStatus());
        userDTO.setRoleId(user.getRole().getRoleId());

        return userDTO;
    }
}
