package com.github.missioncriticalcloud.cosmic.api.usage.services;

import java.util.List;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import org.joda.time.DateTime;

public interface SearchService {

    List<Domain> search(DateTime from, DateTime to, String path);

}
