package com.github.missioncriticalcloud.cosmic.api.usage.services;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Report;
import org.joda.time.DateTime;

public interface UsageService {

    Report calculateGeneralUsage(DateTime from, DateTime to, String path);

    Report calculateDetailedUsage(DateTime from, DateTime to, String path);

}
