package com.onclass.technology.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("technology")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TechnologyEntity {
    @Id
    @Column("technology_id")
    private Long id;
    private String name;
    private String description;
}
