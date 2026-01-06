package com.onclass.technology.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("capability_technology")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CapabilityTechnologyEntity {
    @Id
    @Column("capability_technology_id")
    private Long id;
    @Column("capability_id")
    private Long capabilityId;
    @Column("technology_id")
    private Long technologyId;
}
