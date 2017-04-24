package com.github.missioncriticalcloud.cosmic.api.usage.repositories.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.missioncriticalcloud.cosmic.usage.core.model.PublicIp;
import com.github.missioncriticalcloud.cosmic.usage.core.model.VirtualMachine;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Volume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
public class ResourcesJdbcRepositoryIT {

    @Autowired
    private VirtualMachinesJdbcRepository virtualMachinesRepository;
    @Autowired
    private VolumesJdbcRepository volumesRepository;
    @Autowired
    private PublicIpsJdbcRepository publicIpsRepository;

    @Test
    @Sql(value = {"/test-schema.sql", "/test-virtual-machines-repository-data.sql"})
    public void testVirtualMachineRepository() {
        VirtualMachine virtualMachine = virtualMachinesRepository.get("uuid_not_exists");
        assertThat(virtualMachine).isNull();

        virtualMachine = virtualMachinesRepository.get("vm_instance_uuid1");
        assertThat(virtualMachine).isNotNull();
        assertThat(virtualMachine.getCpu()).isNotNull();
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-volumes-repository-data.sql"})
    public void testVolumesRepository() {
        Volume volume = volumesRepository.get("uuid_not_exists");
        assertThat(volume).isNull();

        volume = volumesRepository.get("storage_uuid1");
        assertThat(volume).isNotNull();
        assertThat(volume.getSize()).isNotNull();
    }

    @Test
    @Sql(value = {"/test-schema.sql", "/test-public-ips-repository-data.sql"})
    public void testPublicIpsRepository() {
        PublicIp publicIp = publicIpsRepository.get("uuid_not_exists");
        assertThat(publicIp).isNull();

        publicIp = publicIpsRepository.get("ip_uuid1");
        assertThat(publicIp).isNotNull();
        assertThat(publicIp.getState()).isNotNull();
    }

}
