package com.trvihnls.exceptions;

import com.trvihnls.enums.ErrorCodeEnum;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;

    public AppException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.errorCodeEnum = errorCodeEnum;
    }
}
