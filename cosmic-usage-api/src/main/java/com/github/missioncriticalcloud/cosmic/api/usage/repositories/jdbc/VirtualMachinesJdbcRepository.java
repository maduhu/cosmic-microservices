package com.github.missioncriticalcloud.cosmic.api.usage.repositories.jdbc;

import java.util.Properties;

import com.github.missioncriticalcloud.cosmic.api.usage.repositories.VirtualMachineRepository;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.jdbc.mappers.VirtualMachineMapper;
import com.github.missioncriticalcloud.cosmic.usage.core.model.VirtualMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VirtualMachinesJdbcRepository implements VirtualMachineRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Properties queries;
    private final VirtualMachineMapper virtualMachineMapper;

    @Autowired
    public VirtualMachinesJdbcRepository(
            final NamedParameterJdbcTemplate jdbcTemplate,
            @Qualifier("queries") final Properties queries,
            final VirtualMachineMapper virtualMachineMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.virtualMachineMapper = virtualMachineMapper;
    }

    @Override
    public VirtualMachine get(final String uuid) {
        return jdbcTemplate.queryForObject(
                queries.getProperty("virtual-machines-repository.get-virtual-machine"),
                new MapSqlParameterSource("uuid", uuid),
                virtualMachineMapper
        );
    }

}
