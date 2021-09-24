package com.example.SpringBootStudy.controller.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ParameterBindingExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        String errorMessage = "{\"errorMessage\":\"invalid argument\"}";
        if (result.hasErrors()) {
            String errorTemplate = "{\"failure\":\"%s\",\"field\":\"%s\",\"message\":\"%s\"}";
            StringBuilder errorFields = new StringBuilder();
            errorFields.append('[');
            result.getFieldErrors().stream().forEach(fieldError -> {
                if (errorFields.length() > 1)
                    errorFields.append(',');
                errorFields.append(String.format(errorTemplate, fieldError.getCode(), fieldError.getField(), fieldError.getDefaultMessage()));
            });
            errorMessage = errorFields.append(']').toString();
        }
        return new ResponseEntity<String>(errorMessage, headers, HttpStatus.BAD_REQUEST);
    }

}
