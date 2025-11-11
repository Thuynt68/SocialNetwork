package com.example.SocialNetwork.controller;



import com.example.SocialNetwork.dto.ApiResponse;
import com.example.SocialNetwork.dto.auth.LoginRequest;
import com.example.SocialNetwork.dto.auth.LoginResponse;
import com.example.SocialNetwork.dto.user.ChangePasswordRequest;
import com.example.SocialNetwork.dto.user.UserDTO;
import com.example.SocialNetwork.service.auth.AuthService;
import com.example.SocialNetwork.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/auth")
public class UserController {
    AuthService authService;
    UserService userService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse result = authService.authenticate(request);
        return ApiResponse.<LoginResponse>builder()
                .result(result)
                .build();
    }


    @PostMapping ("/register")
    public void register(@Valid @RequestBody UserDTO request){
        userService.createUser(request);
    }

    @PostMapping("/register/verify")
    public ApiResponse<Boolean> verifyEmail(@RequestParam String token){
        return ApiResponse.<Boolean>builder()
                .result(userService.verifyEmail(token))
                .build();
    }

    @PutMapping("/change-password")
    public ApiResponse<Map<String, String>> changePassword(@RequestBody ChangePasswordRequest request){
        return ApiResponse.<Map<String, String>>builder()
                .result(userService.changePassword(request))
                .build();
    }
}