package com.onclass.technology.api.capabilitytechnology;

import com.onclass.technology.api.config.CapabilityTechnologyPath;
import com.onclass.technology.api.dto.request.CapabilityTechnologyRequestDto;
import com.onclass.technology.api.dto.response.TechnologySummaryDto;
import com.onclass.technology.api.mapper.TechnologyMapper;
import com.onclass.technology.enums.ExceptionStatusCode;
import com.onclass.technology.model.technology.Technology;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;

@TestPropertySource(properties = {
        "routes.paths.associate-technologies=/technology/api/v1/associate-technologies",
        "routes.paths.get-technologies-by-capability-id=/technology/api/v1/capabilities/{capabilityId}/technologies",
        "routes.paths.delete-technologies-by-capability-ids=/technology/api/v1/capabilities"
})
@ContextConfiguration(classes = {CapabilityTechnologyRouterRest.class, CapabilityTechnologyHandler.class, CapabilityTechnologyPath.class})
@WebFluxTest
class CapabilityTechnologyRouterRestTest {

    private static final String ASSOCIATE_TECHNOLOGIES_PATH = "/technology/api/v1/associate-technologies";
    private static final Long CAPABILITY_ID = 1L;
    private static final List<Long> TECHNOLOGY_IDS = List.of(1L, 2L, 3L);
    private static final String GET_TECHS_PATH = "/technology/api/v1/capabilities/1/technologies";
    private static final String GET_TECHS_PATH_PROPERTY = "/technology/api/v1/capabilities/{capabilityId}/technologies";
    private static final Technology TECH1 = Technology.builder().id(1L).name("Java").build();
    private static final Technology TECH2 = Technology.builder().id(2L).name("Spring").build();
    private static final TechnologySummaryDto TECH_SUMMARY_1 = TechnologySummaryDto.builder().id(1L).name("Java").build();
    private static final TechnologySummaryDto TECH_SUMMARY_2 = TechnologySummaryDto.builder().id(2L).name("Spring").build();
    private static final String DELETE_TECHS_PATH = "/technology/api/v1/capabilities";
    private static final String IDS_PARAM = "ids";
    private static final List<Long> CAPABILITY_IDS = List.of(1L, 2L, 3L);

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CapabilityTechnologyUseCase capabilityTechnologyUseCase;

    @MockitoBean
    private TechnologyMapper technologyMapper;

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

    @Test
    @DisplayName("Should load get-technologies-by-capability-id path property from CapabilityTechnologyPath")
    void shouldLoadGetTechnologiesByCapabilityIdPathProperty() {
        Assertions.assertThat(capabilityTechnologyPath.getTechnologiesByCapabilityId()).isEqualTo(GET_TECHS_PATH_PROPERTY);
    }

    @Test
    @DisplayName("GET /capabilities/{capabilityId}/technologies - should return 200 and list of technologies")
    void getTechnologiesByCapabilityId_shouldReturnTechnologies() {
        when(capabilityTechnologyUseCase.getTechnologiesByCapabilityId(CAPABILITY_ID)).thenReturn(Flux.just(TECH1, TECH2));
        when(technologyMapper.toTechnologySummaryDto(TECH1)).thenReturn(TECH_SUMMARY_1);
        when(technologyMapper.toTechnologySummaryDto(TECH2)).thenReturn(TECH_SUMMARY_2);

        webTestClient.get()
                .uri(GET_TECHS_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(ExceptionStatusCode.OK.status())
                .jsonPath("$.data[0].id").isEqualTo(1)
                .jsonPath("$.data[0].name").isEqualTo("Java")
                .jsonPath("$.data[1].id").isEqualTo(2)
                .jsonPath("$.data[1].name").isEqualTo("Spring");
    }

    @Test
    @DisplayName("DELETE /capabilities - listenDeleteTechnologiesByCapabilityIds: should return 204 when deletion is successful")
    void deleteTechnologiesByCapabilityIds_shouldReturnNoContent() {
        when(capabilityTechnologyUseCase.deleteTechnologiesByCapabilityIds(CAPABILITY_IDS)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(DELETE_TECHS_PATH)
                        .queryParam(IDS_PARAM, "1", "2", "3")
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("DELETE /capabilities - listenDeleteTechnologiesByCapabilityIds: should return 400 when no ids are provided")
    void deleteTechnologiesByCapabilityIds_shouldReturnBadRequestWhenNoIds() {
        webTestClient.delete()
                .uri(DELETE_TECHS_PATH)
                .exchange()
                .expectStatus().isBadRequest();
    }

}
