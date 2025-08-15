package com.trvihnls.controllers;

import com.trvihnls.dtos.auth.*;
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

    @PostMapping(path = "/sign-up")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUpApi(@RequestBody @Valid SignUpRequest request) {
        SignUpResponse response = authService.signUp(request);
        return ResponseUtils.success(SuccessCodeEnum.SIGN_UP_SUCCESS, response);
    }

    @PostMapping(path = "/sign-out")
    public ResponseEntity<ApiResponse<Void>> signOutApi(@RequestBody @Valid SignOutRequest request) {
        authService.signOut(request.getToken());
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }

    @PostMapping(path = "/introspect-token")
    public ResponseEntity<ApiResponse<IntrospectTokenResponse>> introspectTokenApi(@RequestBody @Valid IntrospectTokenRequest request) {
        IntrospectTokenResponse introspectTokenResponse = authService.verifyToken(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, introspectTokenResponse);
    }


}
