package com.smasher.library_management_system.advice;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ApiError {

    private HttpStatus status;
    private String message;
}
