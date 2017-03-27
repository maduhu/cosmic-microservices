package com.github.missioncriticalcloud.cosmic.api.usage.services;

import static com.github.missioncriticalcloud.cosmic.api.usage.utils.FormatUtils.DATE_FORMATTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.List;

import com.github.missioncriticalcloud.cosmic.api.usage.exceptions.NoMetricsFoundException;
import com.github.missioncriticalcloud.cosmic.api.usage.model.Domain;
import io.searchbox.client.JestClient;
import io.searchbox.core.Bulk;
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
public class SearchServiceIT {

    @Autowired
    private JestClient client;

    @Autowired
    private SearchService searchService;

    @Test(expected = NoMetricsFoundException.class)
    public void searchTest1() throws IOException {
        setupIndex();

        final DateTime from = new DateTime();
        final DateTime to = new DateTime();
        final String path = null;

        searchService.search(from, to, path);
    }

    @Test
    public void searchTest2() throws IOException {
        setupIndex();
        setupData();

        final DateTime from = DATE_FORMATTER.parseDateTime("2017-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2017-02-01");
        final String path = null;

        final List<Domain> domains = searchService.search(from, to, path);
        assertDomains(domains, 2);
    }

    @Test
    public void searchTest3() throws IOException {
        setupIndex();
        setupData();

        final DateTime from = DATE_FORMATTER.parseDateTime("2017-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2017-02-01");
        final String path = "/";

        final List<Domain> domains = searchService.search(from, to, path);
        assertDomains(domains, 2);
    }

    @Test
    public void searchTest4() throws IOException {
        setupIndex();
        setupData();

        final DateTime from = DATE_FORMATTER.parseDateTime("2017-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2017-02-01");
        final String path = "/level1";

        final List<Domain> domains = searchService.search(from, to, path);
        assertDomains(domains, 1);
    }

    @Test(expected = NoMetricsFoundException.class)
    public void searchTest5() throws IOException {
        setupIndex();
        setupData();

        final DateTime from = DATE_FORMATTER.parseDateTime("2017-01-01");
        final DateTime to = DATE_FORMATTER.parseDateTime("2017-02-01");
        final String path = "/level1/level2";

        searchService.search(from, to, path);
    }

    private void setupIndex() throws IOException {
        final Resource resource = new ClassPathResource("/cosmic-metrics-template.json");
        final String template = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));

        final PutTemplate putTemplate = new PutTemplate.Builder("cosmic-metrics-template", template).build();
        client.execute(putTemplate);
    }

    private void setupData() throws IOException {
        final DateTime date = DATE_FORMATTER.parseDateTime("2017-01-15");

        client.execute(
                new Bulk.Builder()
                        .defaultIndex("cosmic-metrics-2017.01.15")
                        .defaultType("metric")
                        .addAction(
                                new Index.Builder(
                                        jsonBuilder()
                                        .startObject()
                                                .field("domainUuid", "1")
                                                .field("accountUuid", "1")
                                                .field("resourceUuid", "1")
                                                .field("resourceType", "VirtualMachine")
                                                .field("@timestamp", date.toDate())
                                                .startObject("payload")
                                                        .field("cpu", 2)
                                                        .field("memory", 2048)
                                                        .field("state", "Running")
                                                .endObject()
                                        .endObject()
                                        .string()
                                ).build()
                        )
                        .addAction(
                                new Index.Builder(
                                        jsonBuilder()
                                        .startObject()
                                                .field("domainUuid", "2")
                                                .field("accountUuid", "2")
                                                .field("resourceUuid", "2")
                                                .field("resourceType", "VirtualMachine")
                                                .field("@timestamp", date.toDate())
                                                .startObject("payload")
                                                        .field("cpu", 4)
                                                        .field("memory", 4096)
                                                        .field("state", "Running")
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

    private void assertDomains(final List<Domain> domains, final int size) {
        assertThat(domains).isNotNull();
        assertThat(domains).isNotEmpty();
        assertThat(domains).hasSize(size);

        domains.forEach(domain -> {
            assertThat(domain).isNotNull();
            assertThat(domain.getUuid()).isNotNull();
            assertThat(domain.getSampleCount()).isNotNull();
            assertThat(domain.getSampleCount()).isPositive();
            assertThat(domain.getResources().isEmpty());
        });
    }

}
