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
    public static final String STORAGE_AVERAGE_AGGREGATION = "storageAverage";

    public static final String IP_ADDRESSES_COUNT_AGGREGATION = "ipAddressesCount";

    public static final String TIMESTAMP_FIELD = "@timestamp";
    public static final String RESOURCE_TYPE_FIELD = "resourceType";
    public static final String DOMAIN_UUID_FIELD = "domainUuid";
    public static final String RESOURCE_UUID_FIELD = "resourceUuid";

    public static final String PAYLOAD_CPU_FIELD = "payload.cpu";
    public static final String PAYLOAD_MEMORY_FIELD = "payload.memory";
    public static final String PAYLOAD_SIZE_FIELD = "payload.size";
    public static final String PAYLOAD_STATE_FIELD = "payload.state";

    private final JestClient client;

    public ResourcesEsRepository(final JestClient client) {
        this.client = client;
    }

    protected SearchResult search(final SearchSourceBuilder searchSourceBuilder) {
        try {
            return client.execute(
                    new Search.Builder(searchSourceBuilder.toString())
                            .addIndex("cosmic-metrics-*")
                            .addType("metric")
                            .build()
            );
        } catch (IOException e) {
            throw new UnableToSearchIndexException(e.getMessage(), e);
        }
    }

}
