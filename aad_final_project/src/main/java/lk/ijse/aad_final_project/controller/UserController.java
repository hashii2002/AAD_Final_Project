package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.AuthDTO;
import lk.ijse.aad_final_project.dto.UserDTO;
import lk.ijse.aad_final_project.dto.UserDataDTO;
import lk.ijse.aad_final_project.security.JwtUtil;
import lk.ijse.aad_final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse login(@RequestBody AuthDTO authDTO) {

        UserDTO userDetails = userService.getUserDetails(authDTO.getUsername(), authDTO.getPassword());

        UserDataDTO userDataDTO = new UserDataDTO();

        userDataDTO.setUserId(userDetails.getUserId());
        userDataDTO.setUsername(userDetails.getUsername());
        userDataDTO.setRole(userDetails.getRoleId().toString());

        String token = jwtUtil.generateToken(userDataDTO);
        userDataDTO.setToken(token);

        return new CommonResponse(0,userDataDTO, "Login Successful");
    }


    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveUser(@RequestBody UserDTO userDTO) {

        userService.saveUser(userDTO);

        return new CommonResponse(0, "User Saved Successfully");
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllUsers() {

        List<UserDTO> allUsers = userService.getAllUsers();

        return new CommonResponse(0, allUsers, "Get All Users API Successful"
        );
    }


    @GetMapping(value = "/select/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectUser(@PathVariable Long userId) {

        UserDTO userDTO = userService.selectUser(userId);
        return new CommonResponse(0, userDTO, "User Selected Successfully");
    }


    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateUser(@RequestBody UserDTO userDTO) {

        userService.updateUser(userDTO);
        return new CommonResponse(0, "User Updated Successfully");
    }


    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteUser(@PathVariable Long userId) {

        userService.deleteUser(userId);
        return new CommonResponse(0, "User Deleted Successfully");
    }
}
