package com.github.missioncriticalcloud.cosmic.api.usage.repositories.es;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DATE_FORMATTER;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.count;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import java.util.List;
import java.util.Set;

import com.github.missioncriticalcloud.cosmic.api.usage.repositories.ResourcesRepository;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.es.parsers.PublicIpParser;
import com.github.missioncriticalcloud.cosmic.usage.core.model.aggregations.DomainAggregation;
import com.github.missioncriticalcloud.cosmic.usage.core.model.types.ResourceType;
import io.searchbox.client.JestClient;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("networkingRepository")
public class NetworkingEsRepository extends ResourcesEsRepository implements ResourcesRepository {

    private PublicIpParser ipParser;

    @Autowired
    public NetworkingEsRepository(final JestClient client, final PublicIpParser ipParser) {
        super(client);
        this.ipParser = ipParser;
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
                .must(termQuery(RESOURCE_TYPE_FIELD, ResourceType.PUBLIC_IP.getValue()));

        if (!domainUuids.isEmpty()) {
            queryBuilder.must(termsQuery(DOMAIN_UUID_FIELD, domainUuids));
        }

        searchBuilder.query(queryBuilder)
                     .aggregation(terms(DOMAINS_AGGREGATION)
                             .field(DOMAIN_UUID_FIELD)
                             .size(250)
                             .subAggregation(terms(RESOURCES_AGGREGATION)
                                     .field(RESOURCE_UUID_FIELD)
                                     .size(2500)
                                     .subAggregation(count(PUBLIC_IPS_COUNT_AGGREGATION)
                                             .field(PAYLOAD_STATE_FIELD)
                                     )
                             )
                     );

        final SearchResult result = search(searchBuilder);
        return ipParser.parse(result);
    }

}
