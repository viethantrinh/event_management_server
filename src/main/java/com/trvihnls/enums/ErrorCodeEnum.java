package com.trvihnls.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeEnum {
    GENERAL_ERROR(9000, HttpStatus.INTERNAL_SERVER_ERROR, "just error"),
    UNAUTHENTICATED_USER_NOT_FOUND(9001, HttpStatus.UNAUTHORIZED, "sign in failed because the user not found"),
    UNAUTHENTICATED_PASSWORD_NOT_MATCH(9002, HttpStatus.UNAUTHORIZED, "sign in failed because password not matched"),
    UNAUTHENTICATED_GENERATE_TOKEN_ERROR(9003, HttpStatus.UNAUTHORIZED, "sign in failed because token failed to generated");

    private final int code;
    private final HttpStatus status;
    private final String message;
}
