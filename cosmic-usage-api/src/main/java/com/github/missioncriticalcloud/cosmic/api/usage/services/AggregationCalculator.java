package com.github.missioncriticalcloud.cosmic.api.usage.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Unit;
import com.github.missioncriticalcloud.cosmic.usage.core.model.aggregations.ResourceAggregation;

public interface AggregationCalculator<T extends ResourceAggregation> {

    void calculateAndMerge(
            Map<String, Domain> domainsMap,
            BigDecimal expectedSampleCount,
            Unit unit,
            List<T> aggregations,
            boolean detailed
    );

}
