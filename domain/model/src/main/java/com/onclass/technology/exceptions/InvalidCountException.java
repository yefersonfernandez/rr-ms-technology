package com.onclass.technology.exceptions;

import com.onclass.technology.enums.ExceptionStatusCode;

public class InvalidCountException extends BusinessException {
    public InvalidCountException(String message) {
        super(ExceptionStatusCode.BAD_REQUEST, message, 400);
    }
}

