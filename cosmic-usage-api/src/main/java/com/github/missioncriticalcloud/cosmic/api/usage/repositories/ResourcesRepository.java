package com.github.missioncriticalcloud.cosmic.api.usage.repositories;

import java.util.Map;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import org.joda.time.DateTime;

public interface ResourcesRepository {

    void list(Map<String, Domain> domainsMap, DateTime from, DateTime to);

}
