package com.onclass.technology.r2dbc.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TechnologyLogMessages {
    public static final String FIND_TECHNOLOGY_BY_ID = "[DB] findTechnologyById(requested:{}, foundId:{}, foundName:{})";
    public static final String SAVE_TECHNOLOGY = "[DB] saveTechnology: id={}, name={}";
    public static final String FIND_TECHNOLOGY_BY_NAME = "[DB] findTechnologyByName: id={}, name={}";
    public static final String COUNT_BY_IDS = "[DB] countByIds: ids={}, count={}";
    public static final String DELETE_TECHNOLOGIES_BY_IDS = "[DB] deleteTechnologiesByIds: ids={}";
}
