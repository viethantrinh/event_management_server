package com.trvihnls.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeEnum {
    GENERAL_ERROR(9000, HttpStatus.INTERNAL_SERVER_ERROR, "just error"),
    UNAUTHENTICATED_USER_NOT_FOUND(9001, HttpStatus.NOT_FOUND, "sign in failed because the user not found"),
    UNAUTHENTICATED_PASSWORD_NOT_MATCH(9002, HttpStatus.UNAUTHORIZED, "sign in failed because password not matched"),
    UNAUTHENTICATED_GENERATE_TOKEN_ERROR(9003, HttpStatus.UNAUTHORIZED, "sign in failed because token failed to generated"),
    BAD_REQUEST(9004, HttpStatus.BAD_REQUEST, "something wrong with the request body"),
    REGISTERED_FAILED_USER_EXISTED(9005, HttpStatus.CONFLICT, "register failed because user already existed"),
    REGISTERED_FAILED_ROLE_NOT_EXISTED(9006, HttpStatus.NOT_FOUND, "register failed because role not existed");

    private final int code;
    private final HttpStatus status;
    private final String message;
}
