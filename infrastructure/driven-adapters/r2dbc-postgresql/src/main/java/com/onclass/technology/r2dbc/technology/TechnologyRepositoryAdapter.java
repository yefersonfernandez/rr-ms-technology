package com.onclass.technology.r2dbc.technology;

import com.onclass.technology.model.technology.Technology;
import com.onclass.technology.model.technology.gateways.TechnologyRepositoryPort;
import com.onclass.technology.r2dbc.entity.TechnologyEntity;
import com.onclass.technology.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.onclass.technology.r2dbc.constants.TechnologyLogMessages.*;

@Repository
@Slf4j
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
        return super.save(technology)
            .doOnNext(saved -> log.info(SAVE_TECHNOLOGY, saved.getId(), saved.getName()));
    }

    @Override
    public Mono<Technology> findTechnologyByName(String name) {
        return repository.findByNameIgnoreCase(name)
            .map(super::toEntity)
            .doOnNext(found -> log.info(FIND_TECHNOLOGY_BY_NAME, found.getId(), found.getName()));
    }

    @Override
    public Mono<Long> countByIds(List<Long> technologyIds) {
        return repository.countByIdIn(technologyIds)
            .doOnNext(count -> log.info(COUNT_BY_IDS, technologyIds, count));
    }

    @Override
    public Mono<Technology> findTechnologyById(Long technologyId) {
        return repository.findById(technologyId)
            .map(super::toEntity)
            .doOnNext(tech -> log.info(FIND_TECHNOLOGY_BY_ID, technologyId, tech.getId(), tech.getName()));
    }

    @Override
    public Mono<Void> deleteTechnologiesByIds(List<Long> technologyIds) {
        return repository.deleteAllById(technologyIds)
            .doOnSuccess(unused -> log.info(DELETE_TECHNOLOGIES_BY_IDS, technologyIds));
    }
}
