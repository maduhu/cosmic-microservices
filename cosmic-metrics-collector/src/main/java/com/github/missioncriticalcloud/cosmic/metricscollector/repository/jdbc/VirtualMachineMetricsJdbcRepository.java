package com.github.missioncriticalcloud.cosmic.metricscollector.repository.jdbc;

import java.util.List;

import com.github.missioncriticalcloud.cosmic.metricscollector.model.Metric;
import com.github.missioncriticalcloud.cosmic.metricscollector.repository.VirtualMachineMetricsRepository;
import com.github.missioncriticalcloud.cosmic.metricscollector.repository.mappers.VirtualMachineMetricsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VirtualMachineMetricsJdbcRepository implements VirtualMachineMetricsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public VirtualMachineMetricsJdbcRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Metric> getMetrics() {
        final String sql = "SELECT " +
                    "d.uuid domainUuid, a.uuid accountUuid, vm.uuid resourceUuid, " +
                    "vm.state state, so.cpu cpu, so.ram_size memory " +
                "FROM vm_instance vm " +
                "JOIN domain d " +
                    "ON d.id = vm.domain_id " +
                "JOIN account a " +
                    "ON a.id = vm.account_id " +
                "JOIN service_offering so " +
                    "ON so.id = vm.service_offering_id " +
                "WHERE " +
                    "vm.type = 'User' " +
                "AND " +
                    "vm.state IN ('Running', 'Stopped') " +
                "AND " +
                    "vm.removed IS NULL";

        return jdbcTemplate.query(sql, new VirtualMachineMetricsMapper());
    }

}
