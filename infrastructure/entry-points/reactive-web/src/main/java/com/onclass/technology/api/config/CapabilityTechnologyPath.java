package com.onclass.technology.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "routes.paths")
public class CapabilityTechnologyPath {
    private String associateTechnologies;
    private String getTechnologiesByCapabilityId;
    private String deleteTechnologiesByCapabilityIds;

    public String getTechnologiesByCapabilityId() {
        return getTechnologiesByCapabilityId;
    }
}
