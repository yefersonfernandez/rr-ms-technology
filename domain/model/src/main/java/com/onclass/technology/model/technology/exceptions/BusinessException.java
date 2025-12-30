package com.onclass.technology.model.technology.exceptions;


import com.onclass.technology.model.technology.enums.ExceptionStatusCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ExceptionStatusCode statusCode;
    private final int status;

    public BusinessException(ExceptionStatusCode statusCode, String message, int status) {
        super(message);
        this.statusCode = statusCode;
        this.status = status;
    }
}