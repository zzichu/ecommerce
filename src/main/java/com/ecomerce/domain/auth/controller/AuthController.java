package com.ecomerce.domain.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecomerce.common.response.ApiResponse;
import com.ecomerce.domain.auth.dto.LoginRequest;
import com.ecomerce.domain.auth.dto.UserDto;
import com.ecomerce.domain.auth.service.AuthService;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<UserDto>> join(@RequestBody UserDto userDto) {
        authService.registerUser(userDto);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "로그인 성공", userDto);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable("userId") Long userId) {
        UserDto user = authService.getUserById(userId);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "유저 조회 성공", user);
        return ResponseEntity.ok(response);
    }
        
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDto>> login(@RequestBody LoginRequest loginRequest) {
        UserDto user = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        ApiResponse<UserDto> response = new ApiResponse<>(true, "로그인 성공", user);
        return ResponseEntity.badRequest().body(response);
    }
    
    @PatchMapping("/user")
    public ResponseEntity<ApiResponse<UserDto>> withDraw(@RequestParam Long userId) {
        authService.withDraw(userId);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "회원 탈퇴 성공", null);
        return ResponseEntity.ok(response);
    }
    
}

