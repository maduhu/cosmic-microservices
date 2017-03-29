package com.github.missioncriticalcloud.cosmic.api.usage.services;

import java.util.List;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Usage;
import org.joda.time.DateTime;

public interface UsageService {

    List<Usage> calculateDomainsUsage(List<Domain> domains, DateTime from, DateTime to);

}
