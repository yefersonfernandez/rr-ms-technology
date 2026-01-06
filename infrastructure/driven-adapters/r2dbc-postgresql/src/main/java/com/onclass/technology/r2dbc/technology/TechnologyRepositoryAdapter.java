package com.onclass.technology.r2dbc.technology;

import com.onclass.technology.model.technology.Technology;
import com.onclass.technology.model.technology.gateways.TechnologyRepositoryPort;
import com.onclass.technology.r2dbc.entity.TechnologyEntity;
import com.onclass.technology.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class TechnologyRepositoryAdapter extends ReactiveAdapterOperations<
        Technology,
        TechnologyEntity,
        Long,
        TechnologyRepository
        > implements TechnologyRepositoryPort {

    public TechnologyRepositoryAdapter(TechnologyRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Technology.class));
    }

    @Override
    public Mono<Technology> saveTechnology(Technology technology) {
        return super.save(technology);
    }

    @Override
    public Mono<Technology> findTechnologyByName(String name) {
        return repository.findByNameIgnoreCase(name)
                .map(super::toEntity);
    }

    @Override
    public Mono<Long> countByIds(List<Long> technologyIds) {
        return repository.countByIdIn(technologyIds);
    }

    @Override
    public Mono<Technology> findTechnologyById(Long technologyId) {
        return repository.findById(technologyId)
                .map(super::toEntity);
    }
}
