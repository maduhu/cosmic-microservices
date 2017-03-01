package com.github.missioncriticalcloud.cosmic.api.usage.repositories.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.missioncriticalcloud.cosmic.api.usage.model.Domain;
import org.springframework.jdbc.core.RowMapper;

public class DomainMapper implements RowMapper<Domain> {

    @Override
    public Domain mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final Domain domain = new Domain();

        domain.setUuid(resultSet.getString("uuid"));

        return domain;
    }

}
