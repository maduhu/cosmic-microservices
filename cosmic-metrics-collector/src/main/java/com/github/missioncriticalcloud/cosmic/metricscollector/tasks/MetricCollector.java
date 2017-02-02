package com.github.missioncriticalcloud.cosmic.metricscollector.tasks;

import com.github.missioncriticalcloud.cosmic.metricscollector.exceptions.FailedToCollectMetricsException;

public interface MetricCollector {

    void collect() throws FailedToCollectMetricsException;

}
