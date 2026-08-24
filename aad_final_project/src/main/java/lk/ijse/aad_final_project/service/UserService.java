package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.UserDTO;

import java.util.List;

public interface UserService {
    void saveUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    UserDTO selectUser(Long userId);

    void updateUser(UserDTO userDTO);

    void deleteUser(Long userId);

    UserDTO getUserDetails(String username, String password);
}
