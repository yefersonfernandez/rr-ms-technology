package com.onclass.technology.r2dbc.capabilitytechnology;

import com.onclass.technology.r2dbc.entity.CapabilityTechnologyEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface CapabilityTechnologyRepository extends ReactiveCrudRepository<CapabilityTechnologyEntity, Long>, ReactiveQueryByExampleExecutor<CapabilityTechnologyEntity> {
    Mono<Long> countAllByTechnologyIdIn(List<Long> technologyIds);
    Flux<CapabilityTechnologyEntity> findAllByCapabilityId(Long capabilityId);
}
