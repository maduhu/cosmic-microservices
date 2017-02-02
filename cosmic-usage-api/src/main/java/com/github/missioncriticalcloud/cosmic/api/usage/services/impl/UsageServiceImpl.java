package com.github.missioncriticalcloud.cosmic.api.usage.services.impl;

import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.github.missioncriticalcloud.cosmic.api.usage.model.Domain;
import com.github.missioncriticalcloud.cosmic.api.usage.model.Resource;
import com.github.missioncriticalcloud.cosmic.api.usage.model.State;
import com.github.missioncriticalcloud.cosmic.api.usage.model.Usage;
import com.github.missioncriticalcloud.cosmic.api.usage.services.UsageService;
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

        for (final Domain domain : domains) {
            final Usage usage = new Usage();
            usage.setAggregateId(domain.getUuid());

            for (final Resource resource : domain.getResources()) {
                for (final State state : resource.getStates()) {
                    if (State.RUNNING.equals(state.getValue())) {
                        usage.addCpu(
                                state.getCpuAverage()
                                     .multiply(state.getSampleCount())
                                     .divide(expectedSampleCount, BigDecimal.ROUND_HALF_EVEN)
                        );
                        usage.addMemory(
                                state.getMemoryAverage()
                                     .multiply(state.getSampleCount())
                                     .divide(expectedSampleCount, BigDecimal.ROUND_HALF_EVEN)
                        );
                    }
                }
            }

            domainsUsage.add(usage);
        }

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

        return durationInSeconds.divide(intervalInSeconds, BigDecimal.ROUND_HALF_EVEN);
    }

}
