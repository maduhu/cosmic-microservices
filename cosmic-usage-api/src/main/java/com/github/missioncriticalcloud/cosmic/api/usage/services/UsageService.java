package com.github.missioncriticalcloud.cosmic.api.usage.services;

import com.github.missioncriticalcloud.cosmic.usage.core.model.GeneralUsage;
import org.joda.time.DateTime;

public interface UsageService {

    GeneralUsage calculateGeneralUsage(DateTime from, DateTime to, String path);

}
