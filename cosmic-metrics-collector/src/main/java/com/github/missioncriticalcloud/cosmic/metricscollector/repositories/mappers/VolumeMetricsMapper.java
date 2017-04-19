package com.github.missioncriticalcloud.cosmic.metricscollector.repositories.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Metric;
import com.github.missioncriticalcloud.cosmic.usage.core.model.types.ResourceType;
import org.springframework.stereotype.Component;

@Component
public class VolumeMetricsMapper extends MetricsMapper {

    @Override
    public Metric mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final Metric metric = super.mapRow(resultSet, i);
        metric.setResourceType(ResourceType.VOLUME);

        metric.getPayload().put("size", resultSet.getLong("size"));

        return metric;
    }

}
