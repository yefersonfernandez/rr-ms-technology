package com.onclass.technology.api.technology;

import com.onclass.technology.api.config.TechnologyPath;
import com.onclass.technology.api.dto.request.TechnologyRequestDto;
import com.onclass.technology.api.dto.response.TechnologyResponseDto;
import com.onclass.technology.api.mapper.TechnologyMapper;
import com.onclass.technology.api.utils.ValidatorUtil;
import com.onclass.technology.enums.ExceptionStatusCode;
import com.onclass.technology.model.technology.Technology;
import com.onclass.technology.usecase.technology.TechnologyUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@TestPropertySource(properties = {"routes.paths.technologies=/technology/api/v1/technologies"})
@ContextConfiguration(classes = {TechnologyRouterRest.class, TechnologyHandler.class, TechnologyPath.class, ValidatorUtil.class})
@WebFluxTest
class TechnologyRouterRestTest {

    private static final String TECHNOLOGIES_PATH = "/technology/api/v1/technologies";
    private static final String TECHNOLOGY_NAME = "Java";
    private static final String TECHNOLOGY_DESCRIPTION = "Programming language";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TechnologyPath technologyPath;

    @MockitoBean
    private TechnologyUseCase technologyUseCase;
    @MockitoBean
    private TechnologyMapper technologyMapper;
    @MockitoBean
    private ValidatorUtil validatorUtil;

    private TechnologyRequestDto validRequestDto;
    private Technology technology;
    private TechnologyResponseDto technologyResponseDto;

    @BeforeEach
    void setUp() {
        validRequestDto = new TechnologyRequestDto(TECHNOLOGY_NAME, TECHNOLOGY_DESCRIPTION);

        technology = Technology.builder()
                .id(1L)
                .name(TECHNOLOGY_NAME)
                .description(TECHNOLOGY_DESCRIPTION)
                .build();

        technologyResponseDto = new TechnologyResponseDto(1L, TECHNOLOGY_NAME, TECHNOLOGY_DESCRIPTION);
    }

    @Test
    @DisplayName("Should load path property from TechnologyPath")
    void shouldLoadTechnologyPathProperty() {
        Assertions.assertThat(technologyPath.getTechnologies()).isEqualTo(TECHNOLOGIES_PATH);
    }

    @Test
    @DisplayName("POST /technologies - listenSaveTechnology: should return 201 when technology is created")
    void post_saveTechnology_shouldReturnCreated() {
        when(validatorUtil.validate(validRequestDto)).thenReturn(Mono.just(validRequestDto));
        when(technologyMapper.toModel(validRequestDto)).thenReturn(technology);
        when(technologyUseCase.saveTechnology(technology)).thenReturn(Mono.just(technology));
        when(technologyMapper.toTechnologyResponseDto(technology)).thenReturn(technologyResponseDto);

        webTestClient.post()
                .uri(TECHNOLOGIES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validRequestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(ExceptionStatusCode.CREATED.status())
                .jsonPath("$.data.name").isEqualTo(TECHNOLOGY_NAME)
                .jsonPath("$.data.description").isEqualTo(TECHNOLOGY_DESCRIPTION);
    }
}

