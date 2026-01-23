package com.onclass.technology.usecase.technology;

import com.onclass.technology.enums.ExceptionMessages;
import com.onclass.technology.exceptions.TechnologyAlreadyExistsException;
import com.onclass.technology.model.technology.Technology;
import com.onclass.technology.model.technology.gateways.TechnologyRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyUseCaseTest {

    @Mock
    private TechnologyRepositoryPort technologyRepositoryPort;
    @InjectMocks
    private TechnologyUseCase technologyUseCase;

    private static final String TECHNOLOGY_NAME = "TestTech";

    @Test
    void saveTechnology_shouldThrowExceptionWhenTechnologyAlreadyExists() {
        Technology technology = new Technology();
        technology.setName(TECHNOLOGY_NAME);
        when(technologyRepositoryPort.findTechnologyByName(TECHNOLOGY_NAME)).thenReturn(Mono.just(new Technology()));

        StepVerifier.create(technologyUseCase.saveTechnology(technology))
                .expectErrorMatches(throwable -> throwable instanceof TechnologyAlreadyExistsException
                        && throwable.getMessage().equals(ExceptionMessages.TECHNOLOGY_ALREADY_EXISTS.format(TECHNOLOGY_NAME)))
                .verify();
    }

    @Test
    void saveTechnology_shouldSaveSuccessfullyWhenNotExists() {
        Technology technology = new Technology();
        technology.setName(TECHNOLOGY_NAME);
        when(technologyRepositoryPort.findTechnologyByName(TECHNOLOGY_NAME)).thenReturn(Mono.empty());
        when(technologyRepositoryPort.saveTechnology(any())).thenReturn(Mono.just(technology));

        StepVerifier.create(technologyUseCase.saveTechnology(technology))
                .expectNext(technology)
                .verifyComplete();
    }
}


