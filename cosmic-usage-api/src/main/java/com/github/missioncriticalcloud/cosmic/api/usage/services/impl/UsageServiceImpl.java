package com.github.missioncriticalcloud.cosmic.api.usage.services.impl;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.missioncriticalcloud.cosmic.api.usage.exceptions.NoMetricsFoundException;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.DomainsRepository;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.ResourcesRepository;
import com.github.missioncriticalcloud.cosmic.usage.core.model.aggregations.DomainAggregation;
import com.github.missioncriticalcloud.cosmic.api.usage.services.UsageService;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Report;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class UsageServiceImpl implements UsageService {

    private final DomainsRepository domainsRepository;

    private ResourcesRepository computeRepository;
    private ResourcesRepository storageRepository;
    private ResourcesRepository networkingRepository;

    private final String scanInterval;

    @Autowired
    public UsageServiceImpl(
            final DomainsRepository domainsRepository,
            @Qualifier("computeRepository") final ResourcesRepository computeRepository,
            @Qualifier("storageRepository") final ResourcesRepository storageRepository,
            @Qualifier("networkingRepository") final ResourcesRepository networkingRepository,
            @Value("${cosmic.usage-api.scan-interval}") final String scanInterval
    ) {
        this.domainsRepository = domainsRepository;
        this.computeRepository = computeRepository;
        this.storageRepository = storageRepository;
        this.networkingRepository = networkingRepository;
        this.scanInterval = scanInterval;
    }

    @Override
    public Report calculateGeneralUsage(final DateTime from, final DateTime to, final String path) {
        final Map<String, Domain> domainsMap = getDomainsMap(path);
        final Set<String> domainUuids = domainsMap.keySet();

        final List<DomainAggregation> computeDomainAggregations = computeRepository.list(domainUuids, from, to);
        final List<DomainAggregation> storageDomainAggregations = storageRepository.list(domainUuids, from, to);
        final List<DomainAggregation> networkingDomainAggregations = networkingRepository.list(domainUuids, from, to);

        final BigDecimal expectedSampleCount = calculateExpectedSampleCount(from, to);

        mergeComputeDomainAggregations(domainsMap, expectedSampleCount, computeDomainAggregations);
        mergeStorageDomainAggregations(domainsMap, expectedSampleCount, storageDomainAggregations);
        mergeNetworkingDomainAggregations(domainsMap, expectedSampleCount, networkingDomainAggregations);
        cleanUpEmptyDomains(domainsMap);

        if (domainsMap.isEmpty()) {
            throw new NoMetricsFoundException();
        }

        final Report report = new Report();
        report.getDomains().addAll(domainsMap.values());

        return report;
    }

    @Override
    public Report calculateDetailedUsage(final DateTime from, final DateTime to, final String path) {
        return null;
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

    private BigDecimal calculateExpectedSampleCount(final DateTime from, final DateTime to) {
        final Duration duration = new Duration(from, to);
        final BigDecimal durationInSeconds = BigDecimal.valueOf(duration.getStandardSeconds());

        final CronSequenceGenerator cronSequence = new CronSequenceGenerator(scanInterval);
        final Date nextOccurrence = cronSequence.next(new Date());
        final Date followingOccurrence = cronSequence.next(nextOccurrence);

        final Duration interval = new Duration(nextOccurrence.getTime(), followingOccurrence.getTime());
        final BigDecimal intervalInSeconds = BigDecimal.valueOf(interval.getStandardSeconds());

        return durationInSeconds.divide(intervalInSeconds, RoundingMode.UNNECESSARY);
    }

    private void mergeComputeDomainAggregations(
            final Map<String, Domain> domainsMap,
            final BigDecimal expectedSampleCount,
            final List<DomainAggregation> computeDomainAggregations
    ) {
        computeDomainAggregations.forEach(domainAggregation -> {
            final Domain domain = domainsMap.containsKey(domainAggregation.getUuid())
                    ? domainsMap.get(domainAggregation.getUuid())
                    : new Domain(domainAggregation.getUuid());

            domainAggregation.getVirtualMachineAggregations().forEach(
                    virtualMachine -> {
                        domain.getUsage()
                              .getCompute()
                              .getTotal()
                              .addCpu(virtualMachine.getCpuAverage()
                                                    .multiply(virtualMachine.getSampleCount())
                                                    .divide(expectedSampleCount, DEFAULT_ROUNDING_MODE)
                              );
                        domain.getUsage()
                              .getCompute()
                              .getTotal()
                              .addMemory(virtualMachine.getMemoryAverage()
                                                       .multiply(virtualMachine.getSampleCount())
                                                       .divide(expectedSampleCount, DEFAULT_ROUNDING_MODE)
                              );
                    }
            );

            domainsMap.put(domainAggregation.getUuid(), domain);
        });
    }

    private void mergeStorageDomainAggregations(
            final Map<String, Domain> domainsMap,
            final BigDecimal expectedSampleCount,
            final List<DomainAggregation> storageDomainAggregations
    ) {
        storageDomainAggregations.forEach(domainAggregation -> {
            final Domain domain = domainsMap.containsKey(domainAggregation.getUuid())
                    ? domainsMap.get(domainAggregation.getUuid())
                    : new Domain(domainAggregation.getUuid());

            domainAggregation.getVolumeAggregations().forEach(
                    volume -> domain.getUsage()
                                    .getStorage()
                                    .addTotal(volume.getSize()
                                                    .multiply(volume.getSampleCount())
                                                    .divide(expectedSampleCount, DEFAULT_ROUNDING_MODE)
                                    )
            );

            domainsMap.put(domainAggregation.getUuid(), domain);
        });
    }

    private void mergeNetworkingDomainAggregations(
            final Map<String, Domain> domainsMap,
            final BigDecimal expectedSampleCount,
            final List<DomainAggregation> networkDomainAggregations
    ) {
        networkDomainAggregations.forEach(domainAggregation -> {
            final Domain domain = domainsMap.containsKey(domainAggregation.getUuid())
                    ? domainsMap.get(domainAggregation.getUuid())
                    : new Domain(domainAggregation.getUuid());

            domainAggregation.getPublicIpAggregations().forEach(
                    publicIp -> domain.getUsage()
                                      .getNetworking()
                                      .getTotal()
                                      .addPublicIps(publicIp.getCount()
                                                            .multiply(publicIp.getSampleCount())
                                                            .divide(expectedSampleCount, DEFAULT_ROUNDING_MODE)
                                      )
            );

            domainsMap.put(domainAggregation.getUuid(), domain);
        });
    }

    private void cleanUpEmptyDomains(final Map<String, Domain> domainsMap) {
        final Set<String> uuidsToRemove = new HashSet<>();
        domainsMap.forEach((uuid, domain) -> {
            if (domain.getUsage().isEmpty()) {
                uuidsToRemove.add(uuid);
            }
        });
        uuidsToRemove.forEach(domainsMap::remove);
    }

}
