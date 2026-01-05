package com.onclass.technology.api.technology;

import com.onclass.technology.api.config.TechnologyPath;
import com.onclass.technology.api.openapi.TechnologyOpenApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@RequiredArgsConstructor
public class TechnologyRouterRest {

    private final TechnologyPath technologyPath;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(TechnologyHandler handler) {
        return route()
                .POST(technologyPath.getTechnologies(), handler::listenSaveTechnology, TechnologyOpenApi::saveTechnology)
                .build();
    }
}
