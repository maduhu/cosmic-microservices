package com.github.missioncriticalcloud.cosmic.metricscollector.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Metric;

abstract class MetricsRepositoryIT {

    void assertMetrics(final List<Metric> metrics) {
        assertThat(metrics).isNotNull();
        assertThat(metrics).isNotEmpty();
        assertThat(metrics).hasSize(1);

        metrics.forEach(metric -> {
            assertThat(metric).isNotNull();
            assertThat(metric.getDomainUuid()).isNotNull();
            assertThat(metric.getResourceUuid()).isNotNull();
            assertThat(metric.getResourceType()).isNotNull();
            assertThat(metric.getTimestamp()).isNotNull();
            assertThat(metric.getPayload()).isNotNull();
            assertThat(metric.getPayload()).isNotEmpty();
        });
    }

}
