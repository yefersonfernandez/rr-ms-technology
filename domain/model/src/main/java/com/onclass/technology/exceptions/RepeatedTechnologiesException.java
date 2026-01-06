package com.onclass.technology.exceptions;

import com.onclass.technology.enums.ExceptionStatusCode;

public class RepeatedTechnologiesException extends BusinessException {
    public RepeatedTechnologiesException(String message) {
        super(ExceptionStatusCode.CONFLICT, message, 409);
    }
}

