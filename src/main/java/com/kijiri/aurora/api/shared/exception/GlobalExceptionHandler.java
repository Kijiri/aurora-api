package com.kijiri.aurora.api.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleException(BaseException exception) {
        ExceptionResponse response = ExceptionResponse.builder()
                .businessErrorCode(exception.getErrorCode().getCode())
                .businessErrorDescription(exception.getErrorCode().getDescription())
                .error(exception.getMessage())
                .build();

        return ResponseEntity
                .status(exception.getErrorCode().getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exception) {
        Set<String> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toSet());
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .validationErrors(validationErrors)
                        .build());
    }
}
