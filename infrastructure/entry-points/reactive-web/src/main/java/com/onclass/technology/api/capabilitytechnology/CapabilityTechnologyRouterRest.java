package com.onclass.technology.api.capabilitytechnology;

import com.onclass.technology.api.config.CapabilityTechnologyPath;
import com.onclass.technology.api.openapi.CapabilityTechnologyOpenApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@RequiredArgsConstructor
public class CapabilityTechnologyRouterRest {

    private final CapabilityTechnologyPath capabilityTechnologyPath;

    @Bean
    public RouterFunction<ServerResponse> routerFunctionCapabilityTechnology(CapabilityTechnologyHandler handler) {
        return route()
                .POST(capabilityTechnologyPath.getAssociateTechnologies(), handler::listenAssociateTechnologies, CapabilityTechnologyOpenApi::associateTechnologies)
                .GET(capabilityTechnologyPath.getGetTechnologiesByCapabilityId(), handler::getTechnologiesByCapabilityId, CapabilityTechnologyOpenApi::getTechnologiesByCapabilityId)
                .build();
    }
}
