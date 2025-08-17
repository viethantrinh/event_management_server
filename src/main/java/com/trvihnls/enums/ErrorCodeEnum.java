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
    REGISTERED_FAILED_ROLE_NOT_EXISTED(9006, HttpStatus.NOT_FOUND, "register failed because role not existed"),
    UNAUTHORIZED(9007, HttpStatus.FORBIDDEN, "can't access this resource"),
    UNAUTHENTICATED_BAD_TOKEN(9008, HttpStatus.UNAUTHORIZED, "the token is not valid"),
    USER_NOT_EXISTED(9009, HttpStatus.NOT_FOUND, "user not found"),
    USER_EXISTED(9010, HttpStatus.CONFLICT, "user already exists"),
    ROLE_NOT_EXISTED(9011, HttpStatus.NOT_FOUND, "role not found"),
    DUTY_NOT_EXISTED(9012, HttpStatus.NOT_FOUND, "duty not found"),
    DUTY_EXISTED(9013, HttpStatus.CONFLICT, "duty already exists"),
    EVENT_NOT_EXISTED(9014, HttpStatus.NOT_FOUND, "event not found"),
    EVENT_EXISTED(9015, HttpStatus.CONFLICT, "event already exists"),
    USER_ALREADY_ASSIGNED_IN_EVENT(9016, HttpStatus.CONFLICT, "user is already assigned to another duty in this event");

    private final int code;
    private final HttpStatus status;
    private final String message;
}
