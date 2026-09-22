package lk.ijse.aad_final_project.controller;

import jakarta.validation.Valid;
import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.AuthDTO;
import lk.ijse.aad_final_project.dto.UserDTO;
import lk.ijse.aad_final_project.dto.UserDataDTO;
import lk.ijse.aad_final_project.dto.UserProfileUpdateDTO;
import lk.ijse.aad_final_project.security.JwtUtil;
import lk.ijse.aad_final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> login( @Valid @RequestBody AuthDTO authDTO) {

        UserDTO userDetails = userService.getUserDetails(authDTO.getUsername(), authDTO.getPassword());

        UserDataDTO userDataDTO = new UserDataDTO();

        userDataDTO.setUserId(userDetails.getUserId());
        userDataDTO.setUsername(userDetails.getUsername());
        userDataDTO.setRole(userDetails.getRoleId().toString());

        String token = jwtUtil.generateToken(userDataDTO);
        userDataDTO.setToken(token);

        CommonResponse response = new CommonResponse(0, userDataDTO, "Login Successful");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveUser( @Valid @RequestBody UserDTO userDTO) {
        userService.saveUser(userDTO);
        CommonResponse response = new CommonResponse(0, "User Saved Successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllUsers() {
        List<UserDTO> allUsers = userService.getAllUsers();
        CommonResponse response = new CommonResponse(0, allUsers, "Get All Users API Successful");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectUser(@PathVariable Long userId) {
        UserDTO userDTO = userService.selectUser(userId);
        CommonResponse response = new CommonResponse(0, userDTO, "User Selected Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateUser( @Valid @RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
        CommonResponse response = new CommonResponse(0, "User Updated Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        CommonResponse response = new CommonResponse(0, "User Deactivated Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateMyProfile(@Valid @RequestBody UserProfileUpdateDTO dto, Authentication authentication) {
        String currentUsername = authentication.getName();
        userService.updateMyProfile(dto, currentUsername);
        CommonResponse response = new CommonResponse(0, "Profile Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}