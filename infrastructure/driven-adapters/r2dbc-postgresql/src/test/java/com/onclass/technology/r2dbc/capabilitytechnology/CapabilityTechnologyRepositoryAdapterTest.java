package com.onclass.technology.r2dbc.capabilitytechnology;

import com.onclass.technology.r2dbc.entity.CapabilityTechnologyEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapabilityTechnologyRepositoryAdapterTest {

    @InjectMocks
    private CapabilityTechnologyRepositoryAdapter adapter;
    @Mock
    private CapabilityTechnologyRepository repository;
    @Mock
    private TransactionalOperator transactionalOperator;

    @Test
    @DisplayName("saveAll should save all associations transactionally and complete")
    void saveAll_shouldSaveAllAssociationsTransactionally() {
        Long capabilityId = 1L;
        List<Long> technologyIds = List.of(1L, 2L);
        List<CapabilityTechnologyEntity> entities = List.of(
                CapabilityTechnologyEntity.builder().capabilityId(capabilityId).technologyId(1L).build(),
                CapabilityTechnologyEntity.builder().capabilityId(capabilityId).technologyId(2L).build()
        );

        when(repository.saveAll(anyList())).thenReturn(Flux.fromIterable(entities));
        when(transactionalOperator.transactional(ArgumentMatchers.<Mono<Object>>any())).thenAnswer(invocation -> invocation.getArgument(0));

        StepVerifier.create(adapter.saveAll(capabilityId, technologyIds))
                .verifyComplete();
    }

    @Test
    @DisplayName("findTechnologyIdsByCapabilityId should return technology ids")
    void findTechnologyIdsByCapabilityId_shouldReturnIds() {
        Long capabilityId = 1L;
        List<CapabilityTechnologyEntity> entities = List.of(
                CapabilityTechnologyEntity.builder().capabilityId(capabilityId).technologyId(1L).build(),
                CapabilityTechnologyEntity.builder().capabilityId(capabilityId).technologyId(2L).build()
        );
        when(repository.findAllByCapabilityId(capabilityId)).thenReturn(Flux.fromIterable(entities));

        StepVerifier.create(adapter.findTechnologyIdsByCapabilityId(capabilityId))
                .expectNext(1L, 2L)
                .verifyComplete();
    }

    @Test
    @DisplayName("findTechnologyIdsByCapabilityId should return empty when none found")
    void findTechnologyIdsByCapabilityId_shouldReturnEmpty() {
        Long capabilityId = 2L;
        when(repository.findAllByCapabilityId(capabilityId)).thenReturn(Flux.empty());

        StepVerifier.create(adapter.findTechnologyIdsByCapabilityId(capabilityId))
                .verifyComplete();
    }

    @Test
    @DisplayName("findTechnologyIdsByCapabilityIds should return technology ids for multiple capabilities")
    void findTechnologyIdsByCapabilityIds_shouldReturnIds() {
        List<Long> capabilityIds = List.of(1L, 2L);
        List<CapabilityTechnologyEntity> entities = List.of(
                CapabilityTechnologyEntity.builder().capabilityId(1L).technologyId(10L).build(),
                CapabilityTechnologyEntity.builder().capabilityId(2L).technologyId(20L).build()
        );
        when(repository.findAllByCapabilityIdIn(capabilityIds)).thenReturn(Flux.fromIterable(entities));

        StepVerifier.create(adapter.findTechnologyIdsByCapabilityIds(capabilityIds))
                .expectNext(10L, 20L)
                .verifyComplete();
    }

    @Test
    @DisplayName("findTechnologyIdsByCapabilityIds should return empty when none found")
    void findTechnologyIdsByCapabilityIds_shouldReturnEmpty() {
        List<Long> capabilityIds = List.of(3L, 4L);
        when(repository.findAllByCapabilityIdIn(capabilityIds)).thenReturn(Flux.empty());

        StepVerifier.create(adapter.findTechnologyIdsByCapabilityIds(capabilityIds))
                .verifyComplete();
    }

    @Test
    @DisplayName("countOtherCapacityAssociations should return count")
    void countOtherCapacityAssociations_shouldReturnCount() {
        Long technologyId = 1L;
        List<Long> capabilityIds = List.of(2L, 3L);
        when(repository.countByTechnologyIdAndCapabilityIdNotIn(technologyId, capabilityIds)).thenReturn(Mono.just(5L));

        StepVerifier.create(adapter.countOtherCapacityAssociations(technologyId, capabilityIds))
                .expectNext(5L)
                .verifyComplete();
    }

    @Test
    @DisplayName("deleteAssociationsByCapabilityIds should delete and complete transactionally")
    void deleteAssociationsByCapabilityIds_shouldDeleteAndComplete() {
        List<Long> capabilityIds = List.of(1L, 2L);
        when(repository.deleteAllByCapabilityIdIn(capabilityIds)).thenReturn(Mono.empty());
        when(transactionalOperator.transactional(ArgumentMatchers.<Mono<Object>>any())).thenAnswer(invocation -> invocation.getArgument(0));

        StepVerifier.create(adapter.deleteAssociationsByCapabilityIds(capabilityIds))
                .verifyComplete();
    }
}
