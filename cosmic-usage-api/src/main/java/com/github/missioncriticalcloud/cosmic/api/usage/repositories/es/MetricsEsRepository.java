package com.github.missioncriticalcloud.cosmic.api.usage.repositories.es;

import java.io.IOException;

import com.github.missioncriticalcloud.cosmic.api.usage.exceptions.UnableToSearchMetricsException;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.MetricsRepository;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public abstract class MetricsEsRepository implements MetricsRepository {

    static final int MAX_DOMAIN_AGGREGATIONS = 250;
    static final int MAX_RESOURCE_AGGREGATIONS = 2500;

    public static final String DOMAINS_AGGREGATION = "domains";
    public static final String RESOURCES_AGGREGATION = "resources";

    public static final String CPU_AVERAGE_AGGREGATION = "cpuAverage";
    public static final String MEMORY_AVERAGE_AGGREGATION = "memoryAverage";
    public static final String VOLUME_AVERAGE_AGGREGATION = "volumeAverage";

    public static final String TIMESTAMP_FIELD = "@timestamp";
    public static final String RESOURCE_TYPE_FIELD = "resourceType";
    public static final String DOMAIN_UUID_FIELD = "domainUuid";
    public static final String RESOURCE_UUID_FIELD = "resourceUuid";

    static final String PAYLOAD_CPU_FIELD = "payload.cpu";
    static final String PAYLOAD_MEMORY_FIELD = "payload.memory";
    static final String PAYLOAD_SIZE_FIELD = "payload.size";

    private final JestClient client;

    public MetricsEsRepository(final JestClient client) {
        this.client = client;
    }

    SearchResult search(final SearchSourceBuilder searchBuilder) {
        try {
            return client.execute(
                    new Search.Builder(searchBuilder.toString())
                            .addIndex("cosmic-metrics-*")
                            .addType("metric")
                            .build()
            );
        } catch (IOException e) {
            throw new UnableToSearchMetricsException(e.getMessage(), e);
        }
    }

}
