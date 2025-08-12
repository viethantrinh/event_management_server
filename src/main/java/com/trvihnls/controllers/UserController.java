package com.trvihnls.controllers;

import com.trvihnls.dtos.auth.*;
import com.trvihnls.dtos.base.ApiResponse;
import com.trvihnls.dtos.user.UserResponse;
import com.trvihnls.enums.SuccessCodeEnum;
import com.trvihnls.services.AuthService;
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


}
