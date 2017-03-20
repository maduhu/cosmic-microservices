package com.github.missioncriticalcloud.cosmic.metricscollector.repositories.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.missioncriticalcloud.cosmic.metricscollector.model.Metric;
import com.github.missioncriticalcloud.cosmic.metricscollector.model.ResourceType;

public class PublicIpAddressMetricsMapper extends MetricsMapper {

    @Override
    public Metric mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final Metric metric = super.mapRow(resultSet, i);

        metric.setResourceType(ResourceType.PUBLIC_IP_ADDRESS);
        metric.getPayload().put("state", resultSet.getString("state"));

        return metric;
    }
}
