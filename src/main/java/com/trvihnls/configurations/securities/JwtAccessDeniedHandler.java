package com.trvihnls.configurations.securities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.trvihnls.dtos.base.ApiResponse;
import com.trvihnls.enums.ErrorCodeEnum;
import com.trvihnls.utils.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse.ErrorResponse errorResponse = ApiResponse.ErrorResponse.builder()
                .path(request.getServletPath())
                .errors(List.of(accessDeniedException.getMessage()))
                .build();
        ResponseEntity<ApiResponse<Void>> result = ResponseUtils.error(ErrorCodeEnum.UNAUTHORIZED, errorResponse);
        log.error("error", accessDeniedException);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String errorJson = mapper.writeValueAsString(result.getBody());
        response.getWriter().write(errorJson);
        response.flushBuffer();
    }

}
