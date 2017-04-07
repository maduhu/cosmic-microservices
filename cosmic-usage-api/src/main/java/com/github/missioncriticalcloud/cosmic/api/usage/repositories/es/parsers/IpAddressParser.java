package com.github.missioncriticalcloud.cosmic.api.usage.repositories.es.parsers;

import static com.github.missioncriticalcloud.cosmic.api.usage.repositories.es.ResourcesEsRepository.DOMAINS_AGGREGATION;
import static com.github.missioncriticalcloud.cosmic.api.usage.repositories.es.ResourcesEsRepository.IP_ADDRESSES_COUNT_AGGREGATION;
import static com.github.missioncriticalcloud.cosmic.api.usage.repositories.es.ResourcesEsRepository.RESOURCES_AGGREGATION;
import static java.math.BigDecimal.valueOf;

import java.util.Map;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import com.github.missioncriticalcloud.cosmic.usage.core.model.IpAddress;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.core.search.aggregation.ValueCountAggregation;
import org.springframework.stereotype.Component;

@Component
public class IpAddressParser implements Parser {

    public void parse(final Map<String, Domain> domainsMap, final SearchResult searchResult) {
        if (searchResult.getTotal() == 0) {
            return;
        }

        final TermsAggregation domainsAggregation = searchResult.getAggregations().getTermsAggregation(DOMAINS_AGGREGATION);
        domainsAggregation.getBuckets().forEach(domainBucket -> {

            final Domain domain = domainsMap.containsKey(domainBucket.getKey())
                    ? domainsMap.get(domainBucket.getKey())
                    : new Domain(domainBucket.getKey());

            final TermsAggregation resourcesAggregation = domainBucket.getTermsAggregation(RESOURCES_AGGREGATION);
            resourcesAggregation.getBuckets().forEach(resourceBucket -> {

                final IpAddress resource = new IpAddress();
                resource.setUuid(resourceBucket.getKey());
                resource.setSampleCount(valueOf(resourceBucket.getCount()));

                final ValueCountAggregation ipAddressCount = resourceBucket.getValueCountAggregation(IP_ADDRESSES_COUNT_AGGREGATION);

                resource.setIpAddressCount(valueOf(ipAddressCount.getValueCount()));

                domain.getIpAddresses().add(resource);
                domainsMap.put(domain.getUuid(), domain);
            });
        });
    }

}
