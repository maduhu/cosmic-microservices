package com.github.missioncriticalcloud.cosmic.api.usage.services.impl;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.github.missioncriticalcloud.cosmic.api.usage.services.UsageService;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Usage;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {

    private final String scanInterval;

    @Autowired
    public UsageServiceImpl(
            @Value("${cosmic.usage-api.scan-interval:'0 */15 * * * *'}") final String scanInterval
    ) {
        this.scanInterval = scanInterval;
    }

    @Override
    public List<Usage> calculateDomainsUsage(final List<Domain> domains, final DateTime from, final DateTime to) {
        final List<Usage> domainsUsage = new LinkedList<>();
        final BigDecimal expectedSampleCount = calculateExpectedSampleCount(from, to);

        domains.forEach(domain -> {
            final Usage usage = new Usage();
            usage.setDomain(domain);

            domain.getResources().forEach(resource -> {
                    usage.addCpu(
                            resource.getCpuAverage()
                                 .multiply(resource.getSampleCount())
                                 .divide(expectedSampleCount, DEFAULT_ROUNDING_MODE)
                    );
                    usage.addMemory(
                            resource.getMemoryAverage()
                                 .multiply(resource.getSampleCount())
                                 .divide(expectedSampleCount, DEFAULT_ROUNDING_MODE)
                    );
            });

            domainsUsage.add(usage);
        });

        return domainsUsage;
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
