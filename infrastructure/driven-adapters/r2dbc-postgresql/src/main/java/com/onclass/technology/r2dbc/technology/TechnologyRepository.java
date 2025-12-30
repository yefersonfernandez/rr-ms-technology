package com.onclass.technology.r2dbc.technology;

import com.onclass.technology.model.technology.Technology;
import com.onclass.technology.r2dbc.entity.TechnologyEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface TechnologyRepository extends ReactiveCrudRepository<TechnologyEntity, Long>, ReactiveQueryByExampleExecutor<TechnologyEntity> {
    Mono<Technology> findByNameIgnoreCase(String name);
}
