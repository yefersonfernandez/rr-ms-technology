package com.onclass.technology.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityTechnologyRequestDto {
    private Long capabilityId;
    private List<Long> technologyIds;
}

