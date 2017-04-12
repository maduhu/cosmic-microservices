package com.github.missioncriticalcloud.cosmic.api.usage.repositories.es;

import java.io.IOException;

import com.github.missioncriticalcloud.cosmic.api.usage.exceptions.UnableToSearchIndexException;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public abstract class ResourcesEsRepository {

    public static final String DOMAINS_AGGREGATION = "domains";
    public static final String RESOURCES_AGGREGATION = "resources";

    public static final String CPU_AVERAGE_AGGREGATION = "cpuAverage";
    public static final String MEMORY_AVERAGE_AGGREGATION = "memoryAverage";
    public static final String VOLUME_AVERAGE_AGGREGATION = "volumeAverage";
    public static final String PUBLIC_IPS_COUNT_AGGREGATION = "publicIpsCount";

    public static final String TIMESTAMP_FIELD = "@timestamp";
    public static final String RESOURCE_TYPE_FIELD = "resourceType";
    public static final String DOMAIN_UUID_FIELD = "domainUuid";
    public static final String RESOURCE_UUID_FIELD = "resourceUuid";

    static final String PAYLOAD_CPU_FIELD = "payload.cpu";
    static final String PAYLOAD_MEMORY_FIELD = "payload.memory";
    static final String PAYLOAD_SIZE_FIELD = "payload.size";
    static final String PAYLOAD_STATE_FIELD = "payload.state";

    private final JestClient client;

    public ResourcesEsRepository(final JestClient client) {
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
            throw new UnableToSearchIndexException(e.getMessage(), e);
        }
    }

}
