package com.github.missioncriticalcloud.cosmic.metricscollector.repositories;

import java.util.List;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Metric;

public interface MetricsRepository {

    List<Metric> getMetrics();

}
