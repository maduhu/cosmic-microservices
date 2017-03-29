package com.github.missioncriticalcloud.cosmic.metricscollector.repositories.jdbc;

import java.util.List;
import java.util.Properties;

import com.github.missioncriticalcloud.cosmic.metricscollector.repositories.StorageMetricsRepository;
import com.github.missioncriticalcloud.cosmic.metricscollector.repositories.mappers.StorageMetricsMapper;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StorageMetricsJdbcRepository implements StorageMetricsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Properties queries;

    @Autowired
    public StorageMetricsJdbcRepository(final NamedParameterJdbcTemplate jdbcTemplate, @Qualifier("queries") final Properties queries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
    }

    @Override
    public List<Metric> getMetrics() {
        return jdbcTemplate.query(queries.getProperty("storage-metrics-repository.get-metrics"), new StorageMetricsMapper());
    }

}
