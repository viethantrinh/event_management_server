package com.trvihnls.controllers;

import com.trvihnls.dtos.base.ApiResponse;
import com.trvihnls.dtos.user.UserResponse;
import com.trvihnls.dtos.user.UserUpdateRequest;
import com.trvihnls.enums.SuccessCodeEnum;
import com.trvihnls.services.UserService;
import com.trvihnls.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> listAllUsersApi() {
        List<UserResponse> response = userService.getAllUsers();
        if (response.isEmpty()) return ResponseUtils.success(SuccessCodeEnum.NO_CONTENT, response);
        return ResponseUtils.success(SuccessCodeEnum.SIGN_IN_SUCCESS, response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByIdApi(@PathVariable String userId) {
        UserResponse response = userService.getUserById(userId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserApi(
            @PathVariable String userId,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(userId, request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserApi(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }
}
