package com.onclass.technology.model.technology.gateways;

import com.onclass.technology.model.technology.Technology;
import reactor.core.publisher.Mono;

public interface TechnologyRepositoryPort {
    Mono<Technology> saveTechnology(Technology technology);
    Mono<Technology> findTechnologyByName(String name);
}
