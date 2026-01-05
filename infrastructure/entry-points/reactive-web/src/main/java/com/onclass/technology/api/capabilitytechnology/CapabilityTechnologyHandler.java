package com.onclass.technology.api.capabilitytechnology;

import com.onclass.technology.api.dto.request.CapabilityTechnologyRequestDto;
import com.onclass.technology.enums.ExceptionStatusCode;
import com.onclass.technology.usecase.capabilitytechnology.CapabilityTechnologyUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.onclass.technology.api.utils.HandlersResponseUtil.buildBodySuccessResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class CapabilityTechnologyHandler {

    private final CapabilityTechnologyUseCase capabilityTechnologyUseCase;

    public Mono<ServerResponse> listenAssociateTechnologies(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CapabilityTechnologyRequestDto.class)
                .doOnNext(request -> log.info("Received capability-technology association request: {}", request))
                .flatMap(request ->
                        capabilityTechnologyUseCase.associateTechnologies(request.getCapabilityId(), request.getTechnologyIds())
                )
                .then(ServerResponse.created(URI.create(""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(buildBodySuccessResponse(ExceptionStatusCode.CREATED.status(), null))
                );
    }
}

