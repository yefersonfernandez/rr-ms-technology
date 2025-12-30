package com.onclass.technology.api.mapper;

import com.onclass.technology.api.dto.request.TechnologyRequestDto;
import com.onclass.technology.api.dto.response.TechnologyResponseDto;
import com.onclass.technology.model.technology.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TechnologyMapper {
    TechnologyResponseDto toTechnologyResponseDto(Technology technology);
    Technology toModel(TechnologyRequestDto technologyRequestDto);
}
