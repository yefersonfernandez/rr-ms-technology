package com.onclass.technology.usecase.capabilitytechnology;

import com.onclass.technology.enums.ExceptionMessages;
import com.onclass.technology.exceptions.InvalidCountException;
import com.onclass.technology.exceptions.NotFoundException;
import com.onclass.technology.exceptions.RepeatedTechnologiesException;
import com.onclass.technology.model.capabilitytechnology.gateways.CapabilityTechnologyRepositoryPort;
import com.onclass.technology.model.technology.gateways.TechnologyRepositoryPort;
import com.onclass.technology.usecase.utils.CapabilityTechnologyUtils;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import java.util.List;

import static com.onclass.technology.constants.CapabilityConstants.MAX_TECHS;
import static com.onclass.technology.constants.CapabilityConstants.MIN_TECHS;
import static com.onclass.technology.usecase.utils.CapabilityTechnologyUtils.isValidTechnologiesCount;

@RequiredArgsConstructor
public class CapabilityTechnologyUseCase {

    private final CapabilityTechnologyRepositoryPort capabilityTechnologyRepositoryPort;
    private final TechnologyRepositoryPort technologyRepositoryPort;

    public Mono<Void> associateTechnologies(Long capabilityId, List<Long> technologyIds) {
        return Mono.just(technologyIds)
                .filter(ids -> isValidTechnologiesCount(ids, MIN_TECHS, MAX_TECHS))
                .switchIfEmpty(Mono.error(new InvalidCountException(ExceptionMessages.INVALID_COUNT.format(MIN_TECHS, MAX_TECHS))))
                .filter(CapabilityTechnologyUtils::hasNoRepeatedTechnologies)
                .switchIfEmpty(Mono.error(new RepeatedTechnologiesException(ExceptionMessages.REPEATED_TECHS.format())))
                .flatMap(ids -> technologyRepositoryPort.countByIds(ids)
                        .filter(count -> count == ids.size())
                        .switchIfEmpty(Mono.error(new NotFoundException(ExceptionMessages.TECH_NOT_FOUND.format())))
                        .thenReturn(ids))
                .flatMap(ids -> capabilityTechnologyRepositoryPort.saveAll(capabilityId, ids))
                .then();
    }
}
