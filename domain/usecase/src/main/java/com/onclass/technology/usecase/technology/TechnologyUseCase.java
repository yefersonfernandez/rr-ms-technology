package com.onclass.technology.usecase.technology;

import com.onclass.technology.model.technology.Technology;
import com.onclass.technology.model.technology.enums.ExceptionMessages;
import com.onclass.technology.model.technology.exceptions.TechnologyAlreadyExistsException;
import com.onclass.technology.model.technology.gateways.TechnologyRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TechnologyUseCase {

    private final TechnologyRepositoryPort technologyRepositoryPort;

    public Mono<Technology> saveTechnology(Technology technology) {
        return technologyRepositoryPort.findTechnologyByName(technology.getName())
                .flatMap(existing -> Mono.<Technology>error(
                        new TechnologyAlreadyExistsException(
                                ExceptionMessages.TECHNOLOGY_ALREADY_EXISTS.format(technology.getName())
                        )
                ))
                .switchIfEmpty(Mono.defer(() -> technologyRepositoryPort.saveTechnology(technology)));
    }
}