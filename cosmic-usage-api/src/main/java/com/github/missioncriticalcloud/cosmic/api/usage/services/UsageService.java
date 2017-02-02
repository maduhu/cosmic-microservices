package com.github.missioncriticalcloud.cosmic.api.usage.services;

import java.util.List;

import com.github.missioncriticalcloud.cosmic.api.usage.model.Domain;
import com.github.missioncriticalcloud.cosmic.api.usage.model.Usage;
import org.joda.time.DateTime;

public interface UsageService {

    List<Usage> calculateDomainsUsage(List<Domain> domains, DateTime from, DateTime to);

}
