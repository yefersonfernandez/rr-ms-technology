package com.onclass.technology.r2dbc.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CapabilityTechnologyLogMessages {
    public static final String FIND_TECHNOLOGY_IDS_BY_CAPABILITY_ID = "[DB] findTechnologyIdsByCapabilityId({}): technologyId={}";
    public static final String FIND_TECHNOLOGY_IDS_BY_CAPABILITY_IDS = "[DB] findTechnologyIdsByCapabilityIds: technologyId={}";
    public static final String COUNT_OTHER_CAPACITY_ASSOCIATIONS = "[DB] countOtherCapacityAssociations(tech:{}, exclIds:{}): count={}";
    public static final String DELETE_ASSOCIATIONS_BY_CAPABILITY_IDS = "[DB] deleteAssociationsByCapabilityIds para capabilityIds={}";
    public static final String SAVE_ALL_ASSOCIATIONS_COMPLETED = "[DB] saveAll completed for capabilityId={}";
}
