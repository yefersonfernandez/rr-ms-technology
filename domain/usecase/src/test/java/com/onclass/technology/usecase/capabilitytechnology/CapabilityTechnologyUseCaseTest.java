package com.onclass.technology.usecase.capabilitytechnology;

import com.onclass.technology.enums.ExceptionMessages;
import com.onclass.technology.exceptions.InvalidCountException;
import com.onclass.technology.exceptions.NotFoundException;
import com.onclass.technology.exceptions.RepeatedTechnologiesException;
import com.onclass.technology.model.capabilitytechnology.gateways.CapabilityTechnologyRepositoryPort;
import com.onclass.technology.model.technology.Technology;
import com.onclass.technology.model.technology.gateways.TechnologyRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.List;
import static com.onclass.technology.constants.TechnologyConstants.MAX_TECHS;
import static com.onclass.technology.constants.TechnologyConstants.MIN_TECHS;
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

    private static final List<Long> TECH_IDS = List.of(1L, 2L);
    private static final Technology TECH1 = Technology.builder().id(1L).name("Java").build();
    private static final Technology TECH2 = Technology.builder().id(2L).name("Spring").build();

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

    @Test
    void getTechnologiesByCapabilityId_shouldReturnTechnologies() {
        when(capabilityTechnologyRepositoryPort.findTechnologyIdsByCapabilityId(CAPABILITY_ID))
                .thenReturn(Flux.fromIterable(TECH_IDS));
        when(technologyRepositoryPort.findTechnologyById(1L)).thenReturn(Mono.just(TECH1));
        when(technologyRepositoryPort.findTechnologyById(2L)).thenReturn(Mono.just(TECH2));

        StepVerifier.create(capabilityTechnologyUseCase.getTechnologiesByCapabilityId(CAPABILITY_ID))
                .expectNext(TECH1)
                .expectNext(TECH2)
                .verifyComplete();
    }

    @Test
    void getTechnologiesByCapabilityId_shouldReturnEmptyWhenNoTechnologies() {
        when(capabilityTechnologyRepositoryPort.findTechnologyIdsByCapabilityId(CAPABILITY_ID))
                .thenReturn(Flux.empty());

        StepVerifier.create(capabilityTechnologyUseCase.getTechnologiesByCapabilityId(CAPABILITY_ID))
                .verifyComplete();
    }

    @Test
    void getTechnologiesByCapabilityId_shouldSkipMissingTechnologies() {
        when(capabilityTechnologyRepositoryPort.findTechnologyIdsByCapabilityId(CAPABILITY_ID))
                .thenReturn(Flux.fromIterable(TECH_IDS));
        when(technologyRepositoryPort.findTechnologyById(1L)).thenReturn(Mono.empty());
        when(technologyRepositoryPort.findTechnologyById(2L)).thenReturn(Mono.just(TECH2));

        StepVerifier.create(capabilityTechnologyUseCase.getTechnologiesByCapabilityId(CAPABILITY_ID))
                .expectNext(TECH2)
                .verifyComplete();
    }

    @Test
    void getTechnologiesByCapabilityId_shouldPropagateRepositoryError() {
        when(capabilityTechnologyRepositoryPort.findTechnologyIdsByCapabilityId(CAPABILITY_ID)).thenReturn(Flux.fromIterable(TECH_IDS));
        when(technologyRepositoryPort.findTechnologyById(1L)).thenReturn(Mono.error(new RuntimeException("DB error")));
        StepVerifier.create(capabilityTechnologyUseCase.getTechnologiesByCapabilityId(CAPABILITY_ID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("DB error"))
                .verify();
    }

    @Test
    void deleteTechnologiesByCapabilityIds_shouldDeleteOrphanAndAssociations() {
        List<Long> capabilityIds = List.of(1L, 2L);
        List<Long> techIds = List.of(10L, 20L);

        when(capabilityTechnologyRepositoryPort.findTechnologyIdsByCapabilityIds(capabilityIds)).thenReturn(Flux.fromIterable(techIds));
        when(capabilityTechnologyRepositoryPort.countOtherCapacityAssociations(10L, capabilityIds)).thenReturn(Mono.just(0L));
        when(capabilityTechnologyRepositoryPort.countOtherCapacityAssociations(20L, capabilityIds)).thenReturn(Mono.just(1L));
        when(technologyRepositoryPort.deleteTechnologiesByIds(List.of(10L))).thenReturn(Mono.empty());
        when(capabilityTechnologyRepositoryPort.deleteAssociationsByCapabilityIds(capabilityIds)).thenReturn(Mono.empty());

        StepVerifier.create(capabilityTechnologyUseCase.deleteTechnologiesByCapabilityIds(capabilityIds))
                .verifyComplete();
    }

    @Test
    void deleteTechnologiesByCapabilityIds_shouldSkipIfNoOrphans() {
        List<Long> capabilityIds = List.of(1L);
        List<Long> techIds = List.of(30L);
        when(capabilityTechnologyRepositoryPort.findTechnologyIdsByCapabilityIds(capabilityIds)).thenReturn(Flux.fromIterable(techIds));
        when(capabilityTechnologyRepositoryPort.countOtherCapacityAssociations(30L, capabilityIds)).thenReturn(Mono.just(2L));
        when(capabilityTechnologyRepositoryPort.deleteAssociationsByCapabilityIds(capabilityIds)).thenReturn(Mono.empty());

        StepVerifier.create(capabilityTechnologyUseCase.deleteTechnologiesByCapabilityIds(capabilityIds))
                .verifyComplete();
    }
}
