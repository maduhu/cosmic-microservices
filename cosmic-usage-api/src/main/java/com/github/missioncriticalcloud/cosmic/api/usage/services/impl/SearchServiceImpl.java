package com.github.missioncriticalcloud.cosmic.api.usage.services.impl;

import static com.github.missioncriticalcloud.cosmic.api.usage.utils.FormatUtils.DATE_FORMATTER;
import static java.math.BigDecimal.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.avg;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import java.util.List;
import java.util.stream.Collectors;

import com.github.missioncriticalcloud.cosmic.api.usage.exceptions.NoMetricsFoundException;
import com.github.missioncriticalcloud.cosmic.api.usage.model.Domain;
import com.github.missioncriticalcloud.cosmic.api.usage.model.Resource;
import com.github.missioncriticalcloud.cosmic.api.usage.model.SearchResult;
import com.github.missioncriticalcloud.cosmic.api.usage.model.State;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.DomainsRepository;
import com.github.missioncriticalcloud.cosmic.api.usage.services.SearchService;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class SearchServiceImpl implements SearchService {

    private final DomainsRepository domainsRepository;

    private final Client client;

    @Autowired
    public SearchServiceImpl(final DomainsRepository domainsRepository, final Client client) {
        this.domainsRepository = domainsRepository;
        this.client = client;
    }

    @Override
    public SearchResult search(final DateTime from, final DateTime to, final String path) throws NoMetricsFoundException {
        final List<Domain> domains = getDomains(path);

        final SearchRequestBuilder request = client.prepareSearch("cosmic-metrics-*")
                .setTypes("metric")
                .setSize(0);

        final BoolQueryBuilder queryBuilder = boolQuery()
                .must(rangeQuery("@timestamp")
                        .gte(DATE_FORMATTER.print(from))
                        .lt(DATE_FORMATTER.print(to))
                )
                .must(termQuery("resourceType", "VirtualMachine"));

        if (!CollectionUtils.isEmpty(domains)) {
            queryBuilder.must(termsQuery("domainUuid", domains.stream().map(Domain::getUuid).collect(Collectors.toList())));
        }

        final SearchResponse response = request.setQuery(queryBuilder)
                .addAggregation(terms("domains").field("domainUuid").size(250)
                        .subAggregation(terms("resources").field("resourceUuid").size(2500)
                                .subAggregation(terms("states").field("payload.state").size(2)
                                        .subAggregation(avg("cpuAverage").field("payload.cpu"))
                                        .subAggregation(avg("memoryAverage").field("payload.memory"))
                                )
                        )
                )
                .get();

        return parseResponse(response);
    }

    private List<Domain> getDomains(final String path) throws NoMetricsFoundException {
        List<Domain> domains = null;

        if (StringUtils.hasText(path)) {
            domains = domainsRepository.list(path);

            if (CollectionUtils.isEmpty(domains)) {
                throw new NoMetricsFoundException();
            }
        }

        return domains;
    }

    private SearchResult parseResponse(final SearchResponse response) throws NoMetricsFoundException {
        final long totalHits = response.getHits().getTotalHits();
        final SearchResult searchResult = new SearchResult(valueOf(totalHits));

        if (totalHits == 0) {
            throw new NoMetricsFoundException();
        }

        final Terms domains = response.getAggregations().get("domains");
        for (final Bucket domainBucket : domains.getBuckets()) {

            final Domain domain = new Domain();
            domain.setUuid(domainBucket.getKeyAsString());
            domain.setSampleCount(valueOf(domainBucket.getDocCount()));
            searchResult.getDomains().add(domain);

            final Terms resources = domainBucket.getAggregations().get("resources");
            for (final Bucket resourceBucket : resources.getBuckets()) {

                final Resource resource = new Resource();
                resource.setUuid(resourceBucket.getKeyAsString());
                resource.setSampleCount(valueOf(resourceBucket.getDocCount()));
                domain.getResources().add(resource);

                final Terms states = resourceBucket.getAggregations().get("states");
                for (final Bucket stateBucket : states.getBuckets()) {

                    final State state = new State();
                    state.setValue(stateBucket.getKeyAsString());
                    state.setSampleCount(valueOf(stateBucket.getDocCount()));
                    resource.getStates().add(state);

                    final Avg cpuAverage = stateBucket.getAggregations().get("cpuAverage");
                    final Avg memoryAverage = stateBucket.getAggregations().get("memoryAverage");

                    state.setCpuAverage(valueOf(cpuAverage.getValue()));
                    state.setMemoryAverage(valueOf(memoryAverage.getValue()));
                }
            }
        }

        return searchResult;
    }

}
