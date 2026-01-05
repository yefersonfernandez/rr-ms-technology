package com.onclass.technology.usecase.utils;

import java.util.List;

public class CapabilityTechnologyUtils {
    private CapabilityTechnologyUtils() {}

    public static boolean isValidTechnologiesCount(List<Long> techIds, int min, int max) {
        return techIds != null && techIds.size() >= min && techIds.size() <= max;
    }

    public static boolean hasNoRepeatedTechnologies(List<Long> techIds) {
        return techIds != null && techIds.stream().distinct().count() == techIds.size();
    }
}

