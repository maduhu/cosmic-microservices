package com.github.missioncriticalcloud.cosmic.api.usage.services;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DATE_FORMATTER;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Usage;
import com.github.missioncriticalcloud.cosmic.usage.core.model.VirtualMachine;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
public class UsageServiceTest {

    @SpyBean
    private UsageService usageService;

    @Test
    public void calculateDomainsUsageTest1() {
        final List<Domain> domains = setupSingleDomain(
                5760,
                3,
                3072
        );

        final DateTime from = DATE_FORMATTER.parseDateTime("2016-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2016-01-02");

        assertSingleDomainUsage(
                usageService.calculateDomainsUsage(domains, from, to),
                2,
                2048
        );
    }

    @Test
    public void calculateDomainsUsageTest2() {
        final List<Domain> domains = setupSingleDomain(
                4320,
                3,
                3072
        );

        final DateTime from = DATE_FORMATTER.parseDateTime("2016-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2016-01-02");

        assertSingleDomainUsage(
                usageService.calculateDomainsUsage(domains, from, to),
                1.5,
                1536
        );
    }

    @Test
    public void calculateDomainsUsageTest3() {
        final List<Domain> domains = setupSingleDomain(
                2160,
                4,
                4096
        );

        final DateTime from = DATE_FORMATTER.parseDateTime("2016-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2016-01-02");

        assertSingleDomainUsage(
                usageService.calculateDomainsUsage(domains, from, to),
                1,
                1024
        );
    }

    @Test
    public void calculateDomainsUsageTest4() {
        final List<Domain> domains = setupMultipleDomains(
                5760,
                3,
                3072,
                4320,
                3,
                3072
        );

        final DateTime from = DATE_FORMATTER.parseDateTime("2016-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2016-01-02");

        assertMultipleDomainsUsage(
                usageService.calculateDomainsUsage(domains, from, to),
                2,
                2048,
                1.5,
                1536
        );
    }

    @Test
    public void calculateDomainsUsageTest5() {
        final List<Domain> domains = setupMultipleDomains(
                4320,
                3,
                3072,
                2160,
                4,
                4096
        );

        final DateTime from = DATE_FORMATTER.parseDateTime("2016-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2016-01-02");

        assertMultipleDomainsUsage(
                usageService.calculateDomainsUsage(domains, from, to),
                1.5,
                1536,
                1,
                1024
        );
    }

    @Test
    public void calculateDomainsUsageTest6() {
        final List<Domain> domains = setupMultipleDomains(
                2160,
                4,
                4096,
                4320,
                3,
                3072
        );

        final DateTime from = DATE_FORMATTER.parseDateTime("2016-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2016-01-02");

        assertMultipleDomainsUsage(
                usageService.calculateDomainsUsage(domains, from, to),
                1,
                1024,
                1.5,
                1536
        );
    }

    private List<Domain> setupSingleDomain(
            final long sampleCount,
            final double cpu,
            final double memory
    ) {
        final List<Domain> domains = new ArrayList<>(1);

        final Domain domain = new Domain();
        domain.setUuid("1");
        domains.add(domain);

        final VirtualMachine resource = new VirtualMachine(
                "1",
                valueOf(sampleCount),
                valueOf(cpu),
                valueOf(memory)
        );
        domain.getResources().add(resource);

        return domains;
    }

    private List<Domain> setupMultipleDomains(
            final long sampleCount1,
            final double cpu1,
            final double memory1,
            final long sampleCount2,
            final double cpu2,
            final double memory2
    ) {
        final List<Domain> domains = new ArrayList<>(2);

        final Domain domain1 = new Domain();
        domain1.setUuid("1");
        domains.add(domain1);

        final VirtualMachine resource1 = new VirtualMachine(
                "1",
                valueOf(sampleCount1),
                valueOf(cpu1),
                valueOf(memory1)
        );
        domain1.getResources().add(resource1);

        final Domain domain2 = new Domain();
        domain2.setUuid("2");
        domains.add(domain2);

        final VirtualMachine resource2 = new VirtualMachine(
                "2",
                valueOf(sampleCount2),
                valueOf(cpu2),
                valueOf(memory2)
        );
        domain2.getResources().add(resource2);

        return domains;
    }

    private void assertSingleDomainUsage(
            final List<Usage> domainsUsage,
            final double expectedCpu,
            final double expectedMemory
    ) {
        assertThat(domainsUsage).isNotEmpty();
        assertThat(domainsUsage).hasSize(1);

        final Usage usage = domainsUsage.get(0);
        assertThat(usage).isNotNull();
        assertThat(usage.getDomain()).isNotNull();
        assertThat(usage.getCpu()).isEqualByComparingTo(valueOf(expectedCpu));
        assertThat(usage.getMemory()).isEqualByComparingTo(valueOf(expectedMemory));
    }

    private void assertMultipleDomainsUsage(
            final List<Usage> domainsUsage,
            final double expectedCpu1,
            final double expectedMemory1,
            final double expectedCpu2,
            final double expectedMemory2
    ) {
        assertThat(domainsUsage).isNotEmpty();
        assertThat(domainsUsage).hasSize(2);

        final Usage usage1 = domainsUsage.get(0);
        assertThat(usage1).isNotNull();
        assertThat(usage1.getDomain()).isNotNull();
        assertThat(usage1.getCpu()).isEqualByComparingTo(valueOf(expectedCpu1));
        assertThat(usage1.getMemory()).isEqualByComparingTo(valueOf(expectedMemory1));

        final Usage usage2 = domainsUsage.get(1);
        assertThat(usage2).isNotNull();
        assertThat(usage2.getDomain()).isNotNull();
        assertThat(usage2.getCpu()).isEqualByComparingTo(valueOf(expectedCpu2));
        assertThat(usage2.getMemory()).isEqualByComparingTo(valueOf(expectedMemory2));
    }

}
