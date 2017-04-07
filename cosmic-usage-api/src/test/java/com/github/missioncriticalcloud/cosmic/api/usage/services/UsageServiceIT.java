package com.github.missioncriticalcloud.cosmic.api.usage.services;

import static com.github.missioncriticalcloud.cosmic.api.usage.repositories.es.ResourcesEsRepository.DOMAIN_UUID_FIELD;
import static com.github.missioncriticalcloud.cosmic.api.usage.repositories.es.ResourcesEsRepository.RESOURCE_TYPE_FIELD;
import static com.github.missioncriticalcloud.cosmic.api.usage.repositories.es.ResourcesEsRepository.RESOURCE_UUID_FIELD;
import static com.github.missioncriticalcloud.cosmic.api.usage.repositories.es.ResourcesEsRepository.TIMESTAMP_FIELD;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DATE_FORMATTER;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.github.missioncriticalcloud.cosmic.api.usage.exceptions.NoMetricsFoundException;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Compute;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import com.github.missioncriticalcloud.cosmic.usage.core.model.GeneralUsage;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Network;
import com.github.missioncriticalcloud.cosmic.usage.core.model.ResourceType;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Usage;
import io.searchbox.client.JestClient;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.indices.Refresh;
import io.searchbox.indices.template.PutTemplate;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
@Sql(value = {"/test-schema.sql", "/test-data.sql"})
public class UsageServiceIT {

    @Autowired
    private JestClient client;

    @Autowired
    private UsageService usageService;

    @Test(expected = NoMetricsFoundException.class)
    public void testNoMetricsInterval1() throws IOException {
        setupIndex();

        final DateTime from = DATE_FORMATTER.parseDateTime("2017-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2017-01-02");
        final String path = null;

        usageService.calculateGeneralUsage(from, to, path);
    }

    @Test(expected = NoMetricsFoundException.class)
    public void testNoMetricsInterval2() throws IOException {
        setupIndex();
        setupData();

        final DateTime from = DATE_FORMATTER.parseDateTime("2000-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2000-01-01");
        final String path = null;

        usageService.calculateGeneralUsage(from, to, path);
    }

    @Test
    public void testRootPath() throws IOException {
        setupIndex();
        setupData();

        final DateTime from = DATE_FORMATTER.parseDateTime("2017-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2017-01-02");
        final String path = "/";

        final GeneralUsage generalUsage = usageService.calculateGeneralUsage(from, to, path);
        assertThat(generalUsage).isNotNull();

        final List<Domain> domains = generalUsage.getDomains();
        assertThat(domains).isNotNull();
        assertThat(domains).isNotEmpty();
        assertThat(domains).hasSize(3);

        assertDomain1(domains);
        assertDomain2(domains);
        assertDomain3(domains);
    }

    @Test
    public void testLevel1Path() throws IOException {
        setupIndex();
        setupData();

        final DateTime from = DATE_FORMATTER.parseDateTime("2017-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2017-01-02");
        final String path = "/level1";

        final GeneralUsage generalUsage = usageService.calculateGeneralUsage(from, to, path);
        assertThat(generalUsage).isNotNull();

        final List<Domain> domains = generalUsage.getDomains();
        assertThat(domains).isNotNull();
        assertThat(domains).isNotEmpty();
        assertThat(domains).hasSize(2);

        assertDomain2(domains);
        assertDomain3(domains);
    }

    @Test
    public void testLevel2Path() throws Exception {
        setupIndex();
        setupData();

        final DateTime from = DATE_FORMATTER.parseDateTime("2017-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2017-01-02");
        final String path = "/level1/level2";

        final GeneralUsage generalUsage = usageService.calculateGeneralUsage(from, to, path);
        assertThat(generalUsage).isNotNull();

        final List<Domain> domains = generalUsage.getDomains();
        assertThat(domains).isNotNull();
        assertThat(domains).isNotEmpty();
        assertThat(domains).hasSize(1);

        assertDomain3(domains);
    }

    private void setupIndex() throws IOException {
        final Resource resource = new ClassPathResource("/cosmic-metrics-template.json");
        final String template = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));

        client.execute(
                new Delete.Builder("cosmic-metrics-*")
                        .build()
        );
        client.execute(
                new PutTemplate.Builder("cosmic-metrics-template", template)
                        .build()
        );
    }

    private void setupData() throws IOException {
        final DateTime timestamp = DATE_FORMATTER.parseDateTime("2017-01-01");

        client.execute(
                new Bulk.Builder()
                        .defaultIndex("cosmic-metrics-2017.01.01")
                        .defaultType("metric")
                        .addAction(
                                new Index.Builder(
                                        jsonBuilder()
                                        .startObject()
                                                .field(DOMAIN_UUID_FIELD, "1")
                                                .field(RESOURCE_TYPE_FIELD, ResourceType.VIRTUAL_MACHINE.getValue())
                                                .field(RESOURCE_UUID_FIELD, "1")
                                                .field(TIMESTAMP_FIELD, timestamp.toDate())
                                                .startObject("payload")
                                                        .field("cpu", 192)
                                                        .field("memory", 38400)
                                                .endObject()
                                        .endObject()
                                        .string()
                                ).build()
                        )
                        .addAction(
                                new Index.Builder(
                                        jsonBuilder()
                                        .startObject()
                                                .field(DOMAIN_UUID_FIELD, "2")
                                                .field(RESOURCE_TYPE_FIELD, ResourceType.VIRTUAL_MACHINE.getValue())
                                                .field(RESOURCE_UUID_FIELD, "2")
                                                .field(TIMESTAMP_FIELD, timestamp.toDate())
                                                .startObject("payload")
                                                        .field("cpu", 384)
                                                        .field("memory", 76800)
                                                .endObject()
                                        .endObject()
                                        .string()
                                ).build()
                        )
                        .addAction(
                                new Index.Builder(
                                        jsonBuilder()
                                        .startObject()
                                                .field(DOMAIN_UUID_FIELD, "1")
                                                .field(RESOURCE_TYPE_FIELD, ResourceType.STORAGE.getValue())
                                                .field(RESOURCE_UUID_FIELD, "3")
                                                .field(TIMESTAMP_FIELD, timestamp.toDate())
                                                .startObject("payload")
                                                        .field("size", 144000)
                                                .endObject()
                                        .endObject()
                                        .string()
                                ).build()
                        )
                        .addAction(
                                new Index.Builder(
                                        jsonBuilder()
                                        .startObject()
                                                .field(DOMAIN_UUID_FIELD, "2")
                                                .field(RESOURCE_TYPE_FIELD, ResourceType.STORAGE.getValue())
                                                .field(RESOURCE_UUID_FIELD, "4")
                                                .field(TIMESTAMP_FIELD, timestamp.toDate())
                                                .startObject("payload")
                                                        .field("size", 288000)
                                                .endObject()
                                        .endObject()
                                        .string()
                                ).build()
                        )
                        .addAction(
                                new Index.Builder(
                                        jsonBuilder()
                                        .startObject()
                                                .field(DOMAIN_UUID_FIELD, "1")
                                                .field(RESOURCE_TYPE_FIELD, ResourceType.PUBLIC_IP_ADDRESS.getValue())
                                                .field(RESOURCE_UUID_FIELD, "5")
                                                .field(TIMESTAMP_FIELD, timestamp.toDate())
                                                .startObject("payload")
                                                        .field("state", "Allocated")
                                                .endObject()
                                        .endObject()
                                        .string()
                                ).build()
                        )
                        .addAction(
                                new Index.Builder(
                                        jsonBuilder()
                                        .startObject()
                                                .field(DOMAIN_UUID_FIELD, "2")
                                                .field(RESOURCE_TYPE_FIELD, ResourceType.PUBLIC_IP_ADDRESS.getValue())
                                                .field(RESOURCE_UUID_FIELD, "6")
                                                .field(TIMESTAMP_FIELD, timestamp.toDate())
                                                .startObject("payload")
                                                        .field("state", "Allocated")
                                                .endObject()
                                        .endObject()
                                        .string()
                                ).build()
                        )
                        .build()
        );

        client.execute(
                new Refresh.Builder()
                        .build()
        );
    }

    private void assertDomain1(final List<Domain> domains) {
        domains.stream().filter(domain -> "1".equals(domain.getUuid())).forEach(domain -> {
            assertThat(domain.getName()).isNotNull();
            assertThat(domain.getName()).isEqualTo("ROOT");
            assertThat(domain.getPath()).isEqualTo("/");

            final Usage usage = domain.getUsage();
            assertThat(usage).isNotNull();

            assertCompute(usage.getCompute(), 2, 400);
            assertThat(usage.getStorage()).isEqualByComparingTo(valueOf(1500));
            assertNetwork(usage.getNetwork(), 0.01);
        });
    }

    private void assertDomain2(final List<Domain> domains) {
        domains.stream().filter(domain -> "2".equals(domain.getUuid())).forEach(domain -> {
            assertThat(domain.getName()).isNotNull();
            assertThat(domain.getName()).isEqualTo("level1");
            assertThat(domain.getPath()).isEqualTo("/level1");

            final Usage usage = domain.getUsage();
            assertThat(usage).isNotNull();

            assertCompute(usage.getCompute(), 4, 800);
            assertThat(usage.getStorage()).isEqualByComparingTo(valueOf(3000));
            assertNetwork(usage.getNetwork(), 0.01);
        });
    }

    private void assertDomain3(final List<Domain> domains) {
        domains.stream().filter(domain -> "3".equals(domain.getUuid())).forEach(domain -> {
            assertThat(domain.getName()).isNotNull();
            assertThat(domain.getName()).isEqualTo("level2");
            assertThat(domain.getPath()).isEqualTo("/level1/level2");

            final Usage usage = domain.getUsage();
            assertThat(usage).isNotNull();

            assertCompute(usage.getCompute(), 0, 0);
            assertThat(usage.getStorage()).isEqualByComparingTo(BigDecimal.ZERO);
            assertNetwork(usage.getNetwork(), 0);
        });
    }

    private void assertCompute(final Compute compute, double expectedCpu, double expectedMemory) {
        assertThat(compute).isNotNull();

        final BigDecimal cpu = compute.getCpu();
        assertThat(cpu).isNotNull();
        assertThat(cpu).isEqualByComparingTo(valueOf(expectedCpu));

        final BigDecimal memory = compute.getMemory();
        assertThat(memory).isNotNull();
        assertThat(memory).isEqualByComparingTo(valueOf(expectedMemory));
    }

    private void assertNetwork(final Network network, double expectedPublicIps) {
        assertThat(network).isNotNull();

        final BigDecimal publicIps = network.getPublicIps();
        assertThat(publicIps).isNotNull();
        assertThat(publicIps).isEqualByComparingTo(valueOf(expectedPublicIps));
    }

}
