package com.example.inventory_service.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends Exception{

    private String message;
    private HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public CustomException() {

    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
