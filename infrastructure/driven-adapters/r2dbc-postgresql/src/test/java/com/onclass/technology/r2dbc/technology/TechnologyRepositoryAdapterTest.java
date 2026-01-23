package com.onclass.technology.r2dbc.technology;

import com.onclass.technology.model.technology.Technology;
import com.onclass.technology.r2dbc.entity.TechnologyEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyRepositoryAdapterTest {

    @InjectMocks
    private TechnologyRepositoryAdapter adapter;
    @Mock
    private TechnologyRepository repository;
    @Mock
    private ObjectMapper mapper;

    private Technology technology;
    private TechnologyEntity technologyEntity;

    @BeforeEach
    void setUp() {
        technology = new Technology();
        technologyEntity = new TechnologyEntity();
    }

    @Test
    @DisplayName("saveTechnology should save and return technology")
    void saveTechnology_shouldSaveAndReturnTechnology() {
        when(mapper.map(technology, TechnologyEntity.class)).thenReturn(technologyEntity);
        when(repository.save(technologyEntity)).thenReturn(Mono.just(technologyEntity));
        when(mapper.map(technologyEntity, Technology.class)).thenReturn(technology);

        StepVerifier.create(adapter.saveTechnology(technology))
                .expectNext(technology)
                .verifyComplete();
    }

    @Test
    @DisplayName("findTechnologyByName should return technology when found")
    void findTechnologyByName_shouldReturnTechnology() {
        String name = "Test Technology";
        when(repository.findByNameIgnoreCase(name)).thenReturn(Mono.just(technologyEntity));
        when(mapper.map(technologyEntity, Technology.class)).thenReturn(technology);

        StepVerifier.create(adapter.findTechnologyByName(name))
                .expectNext(technology)
                .verifyComplete();
    }

    @Test
    @DisplayName("findTechnologyByName should complete empty when not found")
    void findTechnologyByName_shouldReturnEmptyWhenNotFound() {
        String name = "NotFound";
        when(repository.findByNameIgnoreCase(name)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findTechnologyByName(name))
                .verifyComplete();
    }

    @Test
    @DisplayName("countByIds should return count of technologies")
    void countByIds_shouldReturnCount() {
        List<Long> ids = List.of(1L, 2L, 3L);
        Long count = 3L;
        when(repository.countByIdIn(ids)).thenReturn(Mono.just(count));

        StepVerifier.create(adapter.countByIds(ids))
                .expectNext(count)
                .verifyComplete();
    }

    @Test
    @DisplayName("findTechnologyById should return technology when found")
    void findTechnologyById_shouldReturnTechnology() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Mono.just(technologyEntity));
        when(mapper.map(technologyEntity, Technology.class)).thenReturn(technology);

        StepVerifier.create(adapter.findTechnologyById(id))
                .expectNext(technology)
                .verifyComplete();
    }

    @Test
    @DisplayName("findTechnologyById should complete empty when not found")
    void findTechnologyById_shouldReturnEmptyWhenNotFound() {
        Long id = 2L;
        when(repository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findTechnologyById(id))
                .verifyComplete();
    }

    @Test
    @DisplayName("deleteTechnologiesByIds should delete all by ids and complete")
    void deleteTechnologiesByIds_shouldDeleteAllAndComplete() {
        List<Long> ids = List.of(1L, 2L, 3L);
        when(repository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteTechnologiesByIds(ids))
                .verifyComplete();
    }
}
