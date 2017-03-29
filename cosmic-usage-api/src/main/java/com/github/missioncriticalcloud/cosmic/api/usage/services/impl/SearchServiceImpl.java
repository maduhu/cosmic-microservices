package com.github.missioncriticalcloud.cosmic.api.usage.services.impl;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DATE_FORMATTER;
import static java.math.BigDecimal.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.avg;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.missioncriticalcloud.cosmic.api.usage.exceptions.NoMetricsFoundException;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.DomainsRepository;
import com.github.missioncriticalcloud.cosmic.api.usage.services.SearchService;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import com.github.missioncriticalcloud.cosmic.usage.core.model.VirtualMachine;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.AvgAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class SearchServiceImpl implements SearchService {

    private final DomainsRepository domainsRepository;

    private final JestClient client;

    @Autowired
    public SearchServiceImpl(final DomainsRepository domainsRepository, final JestClient client) {
        this.domainsRepository = domainsRepository;
        this.client = client;
    }

    @Override
    public List<Domain> search(final DateTime from, final DateTime to, final String path) {
        final Map<String, Domain> domainsMap = getDomainsMap(path);

        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);

        final BoolQueryBuilder queryBuilder = boolQuery()
                .must(rangeQuery("@timestamp")
                        .gte(DATE_FORMATTER.print(from))
                        .lt(DATE_FORMATTER.print(to))
                )
                .must(termQuery("resourceType", "VirtualMachine"));

        if (!CollectionUtils.isEmpty(domainsMap)) {
            queryBuilder.must(termsQuery("domainUuid", domainsMap.keySet()));
        }

        searchSourceBuilder.query(queryBuilder)
                .aggregation(terms("domains").field("domainUuid").size(250)
                        .subAggregation(terms("resources").field("resourceUuid").size(2500)
                                .subAggregation(avg("cpuAverage").field("payload.cpu"))
                                .subAggregation(avg("memoryAverage").field("payload.memory"))
                        )
                );

        try {
            final SearchResult searchResult = client.execute(
                    new Search.Builder(searchSourceBuilder.toString())
                            .addIndex("cosmic-metrics-*")
                            .addType("metric")
                            .build()
            );
            return parseResponse(domainsMap, searchResult);
        } catch (IOException e) {
            throw new NoMetricsFoundException(e.getMessage(), e);
        }

    }

    private Map<String, Domain> getDomainsMap(final String path) {
        final Map<String, Domain> domainsMap = new HashMap<>();

        if (StringUtils.hasText(path)) {
            final List<Domain> domains = domainsRepository.list(path);

            if (CollectionUtils.isEmpty(domains)) {
                throw new NoMetricsFoundException();
            }

            domainsMap.putAll(domains.stream().collect(Collectors.toMap(Domain::getUuid, Function.identity())));
        }

        return domainsMap;
    }

    private static List<Domain> parseResponse(final Map<String, Domain> domainsMap, final SearchResult searchResult) {
        if (searchResult.getTotal() == 0) {
            throw new NoMetricsFoundException();
        }

        final List<Domain> domains = new ArrayList<>();

        final TermsAggregation domainsAggregation = searchResult.getAggregations().getTermsAggregation("domains");
        domainsAggregation.getBuckets().forEach(domainBucket -> {

            final Domain domain = !CollectionUtils.isEmpty(domainsMap) && domainsMap.containsKey(domainBucket.getKey())
                    ? domainsMap.get(domainBucket.getKey())
                    : new Domain(domainBucket.getKey());

            domain.setSampleCount(valueOf(domainBucket.getCount()));
            domains.add(domain);

            final TermsAggregation resourcesAggregation = domainBucket.getTermsAggregation("resources");
            resourcesAggregation.getBuckets().forEach(resourceBucket -> {

                final VirtualMachine resource = new VirtualMachine();
                resource.setUuid(resourceBucket.getKey());
                resource.setSampleCount(valueOf(resourceBucket.getCount()));

                final AvgAggregation cpuAverage = resourceBucket.getAvgAggregation("cpuAverage");
                final AvgAggregation memoryAverage = resourceBucket.getAvgAggregation("memoryAverage");

                resource.setCpuAverage(valueOf(cpuAverage.getAvg()));
                resource.setMemoryAverage(valueOf(memoryAverage.getAvg()));

                domain.getResources().add(resource);
            });
        });

        return domains;
    }

}
