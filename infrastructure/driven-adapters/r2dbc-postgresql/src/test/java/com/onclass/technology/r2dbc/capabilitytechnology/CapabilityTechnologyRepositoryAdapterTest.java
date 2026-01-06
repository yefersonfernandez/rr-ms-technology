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
}
