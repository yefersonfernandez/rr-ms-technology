package com.onclass.technology.api.technology;

import com.onclass.technology.api.dto.request.TechnologyRequestDto;
import com.onclass.technology.api.mapper.TechnologyMapper;
import com.onclass.technology.api.utils.ValidatorUtil;
import com.onclass.technology.enums.ExceptionStatusCode;
import com.onclass.technology.usecase.technology.TechnologyUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.net.URI;

import static com.onclass.technology.api.constants.TechnologyHandlerLogMessages.*;
import static com.onclass.technology.api.utils.HandlersResponseUtil.buildBodySuccessResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class TechnologyHandler {

    private final TechnologyUseCase technologyUseCase;
    private final TechnologyMapper technologyMapper;
    private final ValidatorUtil validatorUtil;

    public Mono<ServerResponse> listenSaveTechnology(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TechnologyRequestDto.class)
                .doOnNext(technologyRequest -> log.info(RECEIVED_TECHNOLOGY_REQUEST, technologyRequest))
                .flatMap(validatorUtil::validate)
                .map(technologyMapper::toModel)
                .flatMap(technologyUseCase::saveTechnology)
                .map(technologyMapper::toTechnologyResponseDto)
                .flatMap(savedTechnology -> ServerResponse.created(URI.create(""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(buildBodySuccessResponse(ExceptionStatusCode.CREATED.status(), savedTechnology))
                );
    }
}
