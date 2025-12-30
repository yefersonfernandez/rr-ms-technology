package com.onclass.technology.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {

    TECHNOLOGY_ALREADY_EXISTS("Technology with name '%s' already exists");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
