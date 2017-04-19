package com.github.missioncriticalcloud.cosmic.api.usage.repositories.jdbc;

import java.util.Properties;

import com.github.missioncriticalcloud.cosmic.api.usage.repositories.PublicIpsRepository;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.jdbc.mappers.PublicIpMapper;
import com.github.missioncriticalcloud.cosmic.usage.core.model.PublicIp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PublicIpsJdbcRepository implements PublicIpsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Properties queries;
    private final PublicIpMapper publicIpMapper;

    @Autowired
    public PublicIpsJdbcRepository(
            final NamedParameterJdbcTemplate jdbcTemplate,
            @Qualifier("queries") final Properties queries,
            final PublicIpMapper publicIpMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.publicIpMapper = publicIpMapper;
    }

    @Override
    public PublicIp get(final String uuid) {
        return jdbcTemplate.queryForObject(
                queries.getProperty("public-ips-repository.get-public-ip"),
                new MapSqlParameterSource("uuid", uuid),
                publicIpMapper
        );
    }

}
