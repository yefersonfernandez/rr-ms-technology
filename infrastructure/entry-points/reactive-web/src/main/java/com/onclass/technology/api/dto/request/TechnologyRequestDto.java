package com.onclass.technology.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request DTO to create or update a Technology")
public record TechnologyRequestDto(

        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name cannot exceed 50 characters")
        @Schema(description = "Name of the technology", example = "Reactive Spring", maxLength = 50)
        String name,

        @NotBlank(message = "Description is required")
        @Size(max = 90, message = "Description cannot exceed 90 characters")
        @Schema(description = "Description of the technology", example = "A reactive framework for Spring applications", maxLength = 90)
        String description

) {}
