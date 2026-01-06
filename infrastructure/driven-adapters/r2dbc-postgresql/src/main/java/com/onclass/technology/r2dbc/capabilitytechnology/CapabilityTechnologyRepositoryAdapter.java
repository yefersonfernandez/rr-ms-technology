package com.onclass.technology.r2dbc.capabilitytechnology;

import com.onclass.technology.model.capabilitytechnology.CapabilityTechnology;
import com.onclass.technology.model.capabilitytechnology.gateways.CapabilityTechnologyRepositoryPort;
import com.onclass.technology.r2dbc.entity.CapabilityTechnologyEntity;
import com.onclass.technology.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@Repository
public class CapabilityTechnologyRepositoryAdapter extends ReactiveAdapterOperations<
        CapabilityTechnology,
        CapabilityTechnologyEntity,
        Long,
        CapabilityTechnologyRepository
        > implements CapabilityTechnologyRepositoryPort {

    private final TransactionalOperator transactionalOperator;

    @Autowired
    public CapabilityTechnologyRepositoryAdapter(CapabilityTechnologyRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, d -> mapper.map(d, CapabilityTechnology.class));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Void> saveAll(Long capabilityId, List<Long> technologyIds) {
        return Flux.fromIterable(technologyIds)
                .map(techId -> CapabilityTechnologyEntity.builder()
                        .capabilityId(capabilityId)
                        .technologyId(techId)
                        .build())
                .collectList()
                .flatMapMany(repository::saveAll)
                .then()
                .as(transactionalOperator::transactional);
    }
}
