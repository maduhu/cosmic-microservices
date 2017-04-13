package com.github.missioncriticalcloud.cosmic.api.usage.repositories;

import java.util.List;
import java.util.Set;

import com.github.missioncriticalcloud.cosmic.usage.core.model.aggregations.DomainAggregation;
import org.joda.time.DateTime;

public interface ResourcesRepository {

    List<DomainAggregation> list(Set<String> domainUuids, DateTime from, DateTime to);

}
