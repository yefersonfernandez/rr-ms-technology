package com.onclass.technology.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {

    TECHNOLOGY_ALREADY_EXISTS("Technology with name '%s' already exists"),
    INVALID_COUNT("The number of technologies must be between %d and %d"),
    REPEATED_TECHS("There are repeated technologies in the list"),
    TECH_NOT_FOUND("Some technology IDs do not exist");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
