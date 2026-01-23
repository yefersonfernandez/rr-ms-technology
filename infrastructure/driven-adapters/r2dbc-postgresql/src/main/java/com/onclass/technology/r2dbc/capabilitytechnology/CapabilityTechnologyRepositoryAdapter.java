package com.onclass.technology.r2dbc.capabilitytechnology;

import com.onclass.technology.model.capabilitytechnology.CapabilityTechnology;
import com.onclass.technology.model.capabilitytechnology.gateways.CapabilityTechnologyRepositoryPort;
import com.onclass.technology.r2dbc.entity.CapabilityTechnologyEntity;
import com.onclass.technology.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import static com.onclass.technology.r2dbc.constants.CapabilityTechnologyLogMessages.*;

@Repository
@Slf4j
public class CapabilityTechnologyRepositoryAdapter extends ReactiveAdapterOperations<
        CapabilityTechnology,
        CapabilityTechnologyEntity,
        Long,
        CapabilityTechnologyRepository
        > implements CapabilityTechnologyRepositoryPort {

    private final TransactionalOperator transactionalOperator;

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
                .doOnComplete(() -> log.info(SAVE_ALL_ASSOCIATIONS_COMPLETED, capabilityId))
                .then()
                .as(transactionalOperator::transactional);
    }

    @Override
    public Flux<Long> findTechnologyIdsByCapabilityId(Long capabilityId) {
        return repository.findAllByCapabilityId(capabilityId)
                .map(CapabilityTechnologyEntity::getTechnologyId)
                .doOnNext(id -> log.info(FIND_TECHNOLOGY_IDS_BY_CAPABILITY_ID, capabilityId, id));
    }

    @Override
    public Flux<Long> findTechnologyIdsByCapabilityIds(List<Long> capabilityIds) {
        return repository.findAllByCapabilityIdIn(capabilityIds)
                .map(CapabilityTechnologyEntity::getTechnologyId)
                .doOnNext(id -> log.info(FIND_TECHNOLOGY_IDS_BY_CAPABILITY_IDS, id));
    }

    @Override
    public Mono<Long> countOtherCapacityAssociations(Long technologyId, List<Long> capabilityIds) {
        return repository.countByTechnologyIdAndCapabilityIdNotIn(technologyId, capabilityIds)
                .doOnNext(count -> log.info(COUNT_OTHER_CAPACITY_ASSOCIATIONS, technologyId, capabilityIds, count));
    }

    @Override
    public Mono<Void> deleteAssociationsByCapabilityIds(List<Long> capabilityIds) {
        return repository.deleteAllByCapabilityIdIn(capabilityIds)
                .doOnSuccess(unused -> log.info(DELETE_ASSOCIATIONS_BY_CAPABILITY_IDS, capabilityIds))
                .as(transactionalOperator::transactional);
    }
}
