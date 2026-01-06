package com.onclass.technology.model.capabilitytechnology.gateways;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

public interface CapabilityTechnologyRepositoryPort {
    Mono<Void> saveAll(Long capabilityId, List<Long> technologyIds);
    Flux<Long> findTechnologyIdsByCapabilityId(Long capabilityId);
}
