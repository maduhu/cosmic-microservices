package com.github.missioncriticalcloud.cosmic.api.usage.repositories.es;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DATE_FORMATTER;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.count;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import java.util.Map;

import com.github.missioncriticalcloud.cosmic.api.usage.repositories.ResourcesRepository;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.es.parsers.IpAddressParser;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import com.github.missioncriticalcloud.cosmic.usage.core.model.ResourceType;
import io.searchbox.client.JestClient;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("networkRepository")
public class NetworkEsRepository extends ResourcesEsRepository implements ResourcesRepository {

    private IpAddressParser ipParser;

    @Autowired
    public NetworkEsRepository(final JestClient client, final IpAddressParser ipParser) {
        super(client);
        this.ipParser = ipParser;
    }

    @Override
    public void list(final Map<String, Domain> domainsMap, final DateTime from, final DateTime to) {

        final SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        searchBuilder.size(0);

        final BoolQueryBuilder queryBuilder = boolQuery()
                .must(rangeQuery(TIMESTAMP_FIELD)
                        .gte(DATE_FORMATTER.print(from))
                        .lt(DATE_FORMATTER.print(to))
                )
                .must(termQuery(RESOURCE_TYPE_FIELD, ResourceType.PUBLIC_IP_ADDRESS.getValue()));

        if (!domainsMap.isEmpty()) {
            queryBuilder.must(termsQuery(DOMAIN_UUID_FIELD, domainsMap.keySet()));
        }

        searchBuilder.query(queryBuilder)
                     .aggregation(terms(DOMAINS_AGGREGATION)
                             .field(DOMAIN_UUID_FIELD)
                             .size(250)
                             .subAggregation(terms(RESOURCES_AGGREGATION)
                                     .field(RESOURCE_UUID_FIELD)
                                     .size(2500)
                                     .subAggregation(count(IP_ADDRESSES_COUNT_AGGREGATION)
                                             .field(PAYLOAD_STATE_FIELD)
                                     )
                             )
                     );

        final SearchResult result = search(searchBuilder);
        ipParser.parse(domainsMap, result);
    }

}
