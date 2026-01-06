package com.onclass.technology.usecase.capabilitytechnology;

import com.onclass.technology.enums.ExceptionMessages;
import com.onclass.technology.exceptions.InvalidCountException;
import com.onclass.technology.exceptions.NotFoundException;
import com.onclass.technology.exceptions.RepeatedTechnologiesException;
import com.onclass.technology.model.capabilitytechnology.gateways.CapabilityTechnologyRepositoryPort;
import com.onclass.technology.model.technology.gateways.TechnologyRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.List;
import static com.onclass.technology.constants.CapabilityConstants.MAX_TECHS;
import static com.onclass.technology.constants.CapabilityConstants.MIN_TECHS;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapabilityTechnologyUseCaseTest {

    @Mock
    private CapabilityTechnologyRepositoryPort capabilityTechnologyRepositoryPort;
    @Mock
    private TechnologyRepositoryPort technologyRepositoryPort;
    @InjectMocks
    private CapabilityTechnologyUseCase capabilityTechnologyUseCase;

    private static final Long CAPABILITY_ID = 1L;
    private static final List<Long> VALID_TECH_IDS = List.of(1L, 2L, 3L);
    private static final List<Long> REPEATED_TECH_IDS = List.of(1L, 1L, 2L);
    private static final List<Long> EMPTY_TECH_IDS = List.of();

    @Test
    void associateTechnologies_shouldThrowExceptionWhenTechnologiesCountInvalid() {
        StepVerifier.create(capabilityTechnologyUseCase.associateTechnologies(CAPABILITY_ID, EMPTY_TECH_IDS))
                .expectErrorMatches(throwable -> throwable instanceof InvalidCountException
                        && throwable.getMessage().equals(ExceptionMessages.INVALID_COUNT.format(MIN_TECHS, MAX_TECHS)))
                .verify();
    }

    @Test
    void associateTechnologies_shouldThrowExceptionWhenTechnologiesAreRepeated() {
        StepVerifier.create(capabilityTechnologyUseCase.associateTechnologies(CAPABILITY_ID, REPEATED_TECH_IDS))
                .expectErrorMatches(throwable -> throwable instanceof RepeatedTechnologiesException
                        && throwable.getMessage().equals(ExceptionMessages.REPEATED_TECHS.format()))
                .verify();
    }

    @Test
    void associateTechnologies_shouldThrowExceptionWhenTechnologiesNotFound() {
        when(technologyRepositoryPort.countByIds(VALID_TECH_IDS)).thenReturn(Mono.just(2L));
        StepVerifier.create(capabilityTechnologyUseCase.associateTechnologies(CAPABILITY_ID, VALID_TECH_IDS))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException
                        && throwable.getMessage().equals(ExceptionMessages.TECH_NOT_FOUND.format()))
                .verify();
    }

    @Test
    void associateTechnologies_shouldSaveAllSuccessfully() {
        when(technologyRepositoryPort.countByIds(VALID_TECH_IDS)).thenReturn(Mono.just((long) VALID_TECH_IDS.size()));
        when(capabilityTechnologyRepositoryPort.saveAll(CAPABILITY_ID, VALID_TECH_IDS)).thenReturn(Mono.empty());
        StepVerifier.create(capabilityTechnologyUseCase.associateTechnologies(CAPABILITY_ID, VALID_TECH_IDS))
                .verifyComplete();
    }
}
