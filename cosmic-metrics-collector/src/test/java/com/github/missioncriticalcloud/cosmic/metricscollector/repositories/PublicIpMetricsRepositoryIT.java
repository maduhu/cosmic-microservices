package com.github.missioncriticalcloud.cosmic.metricscollector.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Metric;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
public class PublicIpMetricsRepositoryIT extends MetricsRepositoryIT {

    @Autowired
    @Qualifier("publicIpMetricsRepository")
    private MetricsRepository metricsRepository;

    @Test
    @Sql(value = "/test-schema.sql")
    public void testEmptyDatabase() {
        final List<Metric> metrics = metricsRepository.getMetrics();
        assertThat(metrics).isNotNull();
        assertThat(metrics).isEmpty();
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-public-ip-data.sql"})
    public void testNonEmptyDatabase() {
        final List<Metric> metrics = metricsRepository.getMetrics();
        assertMetricsWithoutPayload(metrics);
    }

}
