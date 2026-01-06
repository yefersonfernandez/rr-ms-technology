package com.onclass.technology.api.capabilitytechnology;

import com.onclass.technology.api.config.CapabilityTechnologyPath;
import com.onclass.technology.api.dto.request.CapabilityTechnologyRequestDto;
import com.onclass.technology.enums.ExceptionStatusCode;
import com.onclass.technology.usecase.capabilitytechnology.CapabilityTechnologyUseCase;
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

import java.util.List;

import static org.mockito.Mockito.when;

@TestPropertySource(properties = {"routes.paths.associate-technologies=/technology/api/v1/associate-technologies"})
@ContextConfiguration(classes = {CapabilityTechnologyRouterRest.class, CapabilityTechnologyHandler.class, CapabilityTechnologyPath.class})
@WebFluxTest
class CapabilityTechnologyRouterRestTest {

    private static final String ASSOCIATE_TECHNOLOGIES_PATH = "/technology/api/v1/associate-technologies";
    private static final Long CAPABILITY_ID = 1L;
    private static final List<Long> TECHNOLOGY_IDS = List.of(1L, 2L, 3L);

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CapabilityTechnologyUseCase capabilityTechnologyUseCase;

    @Autowired
    private CapabilityTechnologyPath capabilityTechnologyPath;

    private CapabilityTechnologyRequestDto validRequestDto;

    @BeforeEach
    void setUp() {
        validRequestDto = new CapabilityTechnologyRequestDto(CAPABILITY_ID, TECHNOLOGY_IDS);
    }

    @Test
    @DisplayName("Should load path property from CapabilityTechnologyPath")
    void shouldLoadCapabilityTechnologyPathProperty() {
        Assertions.assertThat(capabilityTechnologyPath.getAssociateTechnologies()).isEqualTo(ASSOCIATE_TECHNOLOGIES_PATH);
    }

    @Test
    @DisplayName("POST /associate-technologies - listenAssociateTechnologies: should return 201 when association is created")
    void post_associateTechnologies_shouldReturnCreated() {
        when(capabilityTechnologyUseCase.associateTechnologies(CAPABILITY_ID, TECHNOLOGY_IDS)).thenReturn(Mono.empty());

        webTestClient.post()
                .uri(ASSOCIATE_TECHNOLOGIES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validRequestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(ExceptionStatusCode.CREATED.status());
    }
}
