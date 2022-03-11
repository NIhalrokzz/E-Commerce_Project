package com.example.cart_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customErrorResponse (CustomException ex){
        ErrorResponse er = new ErrorResponse(ex.getMessage(), ex.getStatus());
        return new ResponseEntity<>(er, er.getStatus());
    }
}
