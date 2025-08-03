package com.trvihnls.controllers;

import com.trvihnls.dtos.auth.SignInRequest;
import com.trvihnls.dtos.auth.SignInResponse;
import com.trvihnls.dtos.base.ApiResponse;
import com.trvihnls.enums.SuccessCodeEnum;
import com.trvihnls.services.AuthService;
import com.trvihnls.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/sign-in")
    public ResponseEntity<ApiResponse<SignInResponse>> signInApi(@RequestBody @Valid SignInRequest request) {
        SignInResponse response = authService.signIn(request);
        return ResponseUtils.success(SuccessCodeEnum.SIGN_IN_SUCCESS, response);
    }

}
