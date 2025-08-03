package com.trvihnls.utils;

import com.trvihnls.dtos.base.ApiResponse;
import com.trvihnls.enums.ErrorCodeEnum;
import com.trvihnls.enums.SuccessCodeEnum;
import org.springframework.http.ResponseEntity;


import java.time.LocalDateTime;

public class ResponseUtils {
    public static <T> ResponseEntity<ApiResponse<T>> success(
            SuccessCodeEnum successCodeEnum,
            T data) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(successCodeEnum.getCode())
                .message(successCodeEnum.getMessage())
                .timestamp(LocalDateTime.now())
                .errorResponse(null)
                .result(data)
                .build();
        return new ResponseEntity<>(response, successCodeEnum.getStatus());
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(
            ErrorCodeEnum errorCodeEnum,
            ApiResponse.ErrorResponse errorResponse) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(errorCodeEnum.getCode())
                .message(errorCodeEnum.getMessage())
                .timestamp(LocalDateTime.now())
                .result(null)
                .errorResponse(errorResponse)
                .build();

        return new ResponseEntity<>(response, errorCodeEnum.getStatus());
    }
}
