package com.github.missioncriticalcloud.cosmic.metricscollector.repositories.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Metric;
import com.github.missioncriticalcloud.cosmic.usage.core.model.ResourceType;

public class StorageMetricsMapper extends MetricsMapper{

    @Override
    public Metric mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final Metric metric = super.mapRow(resultSet, i);

        metric.setResourceType(ResourceType.STORAGE);
        metric.getPayload().put("size", resultSet.getString("size"));

        return metric;
    }

}
