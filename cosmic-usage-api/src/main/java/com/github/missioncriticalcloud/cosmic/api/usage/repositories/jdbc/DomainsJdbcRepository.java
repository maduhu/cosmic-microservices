package com.github.missioncriticalcloud.cosmic.api.usage.repositories.jdbc;

import javax.validation.constraints.NotNull;
import java.util.List;

import com.github.missioncriticalcloud.cosmic.api.usage.model.Domain;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.DomainsRepository;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.mappers.DomainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

@Repository
@Validated
public class DomainsJdbcRepository implements DomainsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public DomainsJdbcRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Domain> list(@NotNull final String path) {
        final String sql = "SELECT " +
                    "uuid, name, path " +
                "FROM domain " +
                "WHERE " +
                    "state = 'Active' " +
                "AND " +
                    "removed IS NULL " +
                "AND " +
                    "path LIKE :path";

        return jdbcTemplate.query(sql, new MapSqlParameterSource("path", path + "%"), new DomainMapper());
    }

}
