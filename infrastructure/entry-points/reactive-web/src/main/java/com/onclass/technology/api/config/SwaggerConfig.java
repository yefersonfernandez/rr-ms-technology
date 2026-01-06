package com.onclass.technology.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Technology Microservice",
                description = "Manages technologies and associates capabilities."
        )
)
public class SwaggerConfig {
}
