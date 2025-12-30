package com.onclass.technology.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO representing a Technology")
public record TechnologyResponseDto(

        @Schema(description = "Unique identifier of the technology", example = "1")
        Long id,

        @Schema(description = "Name of the technology", example = "Reactive Spring")
        String name,

        @Schema(description = "Description of the technology", example = "A reactive framework for Spring applications")
        String description

) {}
