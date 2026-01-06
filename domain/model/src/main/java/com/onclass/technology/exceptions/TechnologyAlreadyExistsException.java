package com.onclass.technology.exceptions;

import com.onclass.technology.enums.ExceptionStatusCode;

public class TechnologyAlreadyExistsException extends BusinessException {

    public TechnologyAlreadyExistsException(String message) {
        super(ExceptionStatusCode.CONFLICT, message, 409);
    }

}
