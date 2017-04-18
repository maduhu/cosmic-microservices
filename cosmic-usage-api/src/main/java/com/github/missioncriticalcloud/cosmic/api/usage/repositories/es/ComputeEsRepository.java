package com.github.missioncriticalcloud.cosmic.api.usage.repositories.es;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DATE_FORMATTER;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.avg;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import java.util.List;
import java.util.Set;

import com.github.missioncriticalcloud.cosmic.api.usage.repositories.ResourcesRepository;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.es.parsers.VirtualMachineParser;
import com.github.missioncriticalcloud.cosmic.usage.core.model.aggregations.DomainAggregation;
import com.github.missioncriticalcloud.cosmic.usage.core.model.types.ResourceType;
import io.searchbox.client.JestClient;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("computeRepository")
public class ComputeEsRepository extends ResourcesEsRepository implements ResourcesRepository {

    private VirtualMachineParser virtualMachineParser;

    @Autowired
    public ComputeEsRepository(final JestClient client, final VirtualMachineParser virtualMachineParser) {
        super(client);
        this.virtualMachineParser = virtualMachineParser;
    }

    @Override
    public List<DomainAggregation> list(final Set<String> domainUuids, final DateTime from, final DateTime to) {

        final SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        searchBuilder.size(0);

        final BoolQueryBuilder queryBuilder = boolQuery()
                .must(rangeQuery(TIMESTAMP_FIELD)
                        .gte(DATE_FORMATTER.print(from))
                        .lt(DATE_FORMATTER.print(to))
                )
                .must(termQuery(RESOURCE_TYPE_FIELD, ResourceType.VIRTUAL_MACHINE.getValue()));

        if (!domainUuids.isEmpty()) {
            queryBuilder.must(termsQuery(DOMAIN_UUID_FIELD, domainUuids));
        }

        searchBuilder.query(queryBuilder)
                     .aggregation(terms(DOMAINS_AGGREGATION)
                             .field(DOMAIN_UUID_FIELD)
                             .size(MAX_DOMAIN_AGGREGATIONS)
                             .subAggregation(terms(RESOURCES_AGGREGATION)
                                     .field(RESOURCE_UUID_FIELD)
                                     .size(MAX_RESOURCE_AGGREGATIONS)
                                     .subAggregation(avg(CPU_AVERAGE_AGGREGATION)
                                             .field(PAYLOAD_CPU_FIELD)
                                     )
                                     .subAggregation(avg(MEMORY_AVERAGE_AGGREGATION)
                                             .field(PAYLOAD_MEMORY_FIELD)
                                     )
                             )
                     );

        final SearchResult result = search(searchBuilder);
        return virtualMachineParser.parse(result);
    }

}
