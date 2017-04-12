package com.github.missioncriticalcloud.cosmic.api.usage.repositories.jdbc;

import java.util.List;
import java.util.Properties;

import com.github.missioncriticalcloud.cosmic.api.usage.repositories.DomainsRepository;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.jdbc.mappers.DomainMapper;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DomainsJdbcRepository implements DomainsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Properties queries;
    private final DomainMapper domainMapper;

    @Autowired
    public DomainsJdbcRepository(
            final NamedParameterJdbcTemplate jdbcTemplate,
            @Qualifier("queries") final Properties queries,
            final DomainMapper domainMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.domainMapper = domainMapper;
    }

    @Override
    public List<Domain> list(final String path) {
        return jdbcTemplate.query(queries.getProperty("domains-repository.list-domains"), new MapSqlParameterSource("path", path + "%"), domainMapper);
    }

}
