package com.github.missioncriticalcloud.cosmic.api.usage.services.impl;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.missioncriticalcloud.cosmic.api.usage.exceptions.NoMetricsFoundException;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.DomainsRepository;
import com.github.missioncriticalcloud.cosmic.api.usage.repositories.ResourcesRepository;
import com.github.missioncriticalcloud.cosmic.api.usage.services.UsageService;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import com.github.missioncriticalcloud.cosmic.usage.core.model.GeneralUsage;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Usage;
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
    private ResourcesRepository networkRepository;

    private final String scanInterval;

    @Autowired
    public UsageServiceImpl(
            final DomainsRepository domainsRepository,
            @Qualifier("computeRepository") final ResourcesRepository computeRepository,
            @Qualifier("storageRepository") final ResourcesRepository storageRepository,
            @Qualifier("networkRepository") final ResourcesRepository networkRepository,
            @Value("${cosmic.usage-api.scan-interval}") final String scanInterval
    ) {
        this.domainsRepository = domainsRepository;
        this.computeRepository = computeRepository;
        this.storageRepository = storageRepository;
        this.networkRepository = networkRepository;
        this.scanInterval = scanInterval;
    }

    @Override
    public GeneralUsage calculateGeneralUsage(final DateTime from, final DateTime to, final String path) {
        final GeneralUsage generalUsage = buildGeneralUsage(from, to, path);
        final BigDecimal expectedSampleCount = calculateExpectedSampleCount(from, to);

        generalUsage.getDomains().forEach(domain -> {
            domain.getVirtualMachines().forEach(resource -> {
                domain.getUsage().getCompute().addCpu(
                        resource.getCpuAverage()
                                .multiply(resource.getSampleCount())
                                .divide(expectedSampleCount, DEFAULT_ROUNDING_MODE)
                );
                domain.getUsage().getCompute().addMemory(
                        resource.getMemoryAverage()
                                .multiply(resource.getSampleCount())
                                .divide(expectedSampleCount, DEFAULT_ROUNDING_MODE)
                );
            });

            domain.getStorageItems().forEach(resource -> {
                domain.getUsage().addStorage(
                        resource.getStorageSum()
                                .multiply(resource.getSampleCount())
                                .divide(expectedSampleCount, DEFAULT_ROUNDING_MODE)
                );
            });

            domain.getIpAddresses().forEach(resource -> {
                domain.getUsage().getNetwork().addPublicIps(
                        resource.getIpAddressCount()
                                .multiply(resource.getSampleCount())
                                .divide(expectedSampleCount, DEFAULT_ROUNDING_MODE)
                );
            });
        });

        return generalUsage;
    }

    private GeneralUsage buildGeneralUsage(final DateTime from, final DateTime to, final String path) {
        final Map<String, Domain> domainsMap = getDomainsMap(path);

        computeRepository.list(domainsMap, from, to);
        storageRepository.list(domainsMap, from, to);
        networkRepository.list(domainsMap, from, to);

        if (domainsMap.isEmpty()) {
            throw new NoMetricsFoundException();
        }

        final GeneralUsage generalUsage = new GeneralUsage();
        generalUsage.getDomains().addAll(domainsMap.values());

        return generalUsage;
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
        final BigDecimal durationInSeconds = valueOf(duration.getStandardSeconds());

        final CronSequenceGenerator cronSequence = new CronSequenceGenerator(scanInterval);
        final Date nextOccurrence = cronSequence.next(new Date());
        final Date followingOccurrence = cronSequence.next(nextOccurrence);

        final Duration interval = new Duration(nextOccurrence.getTime(), followingOccurrence.getTime());
        final BigDecimal intervalInSeconds = valueOf(interval.getStandardSeconds());

        return durationInSeconds.divide(intervalInSeconds, RoundingMode.UNNECESSARY);
    }

}
