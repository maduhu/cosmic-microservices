package com.github.missioncriticalcloud.cosmic.metricscollector.tasks;

import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.missioncriticalcloud.cosmic.metricscollector.exceptions.FailedToCollectMetricsException;
import com.github.missioncriticalcloud.cosmic.metricscollector.repositories.PublicIpAddressMetricsRepository;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Metric;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@ConditionalOnExpression("${cosmic.metrics-collector.enabled-collectors:true}")
public class PublicIpAddressMetricsCollector implements MetricsCollector {

    private static final Logger LOG = Logger.getLogger(PublicIpAddressMetricsCollector.class.getName());

    private final PublicIpAddressMetricsRepository publicIpAddressMetricsRepository;
    private final AmqpTemplate amqpTemplate;
    private final ObjectWriter metricWriter;

    private final String brokerExchange;
    private final String brokerExchangeKey;

    @Autowired
    public PublicIpAddressMetricsCollector(
            final PublicIpAddressMetricsRepository publicIpAddressMetricsRepository,
            final AmqpTemplate amqpTemplate,
            final ObjectWriter metricWriter,

            @Value("${cosmic.metrics-collector.broker-exchange}")
            final String brokerExchange,

            @Value("${cosmic.metrics-collector.broker-exchange-key}")
            final String brokerExchangeKey
    ) {
        this.publicIpAddressMetricsRepository = publicIpAddressMetricsRepository;
        this.amqpTemplate = amqpTemplate;
        this.metricWriter = metricWriter;

        this.brokerExchange = brokerExchange;
        this.brokerExchangeKey = brokerExchangeKey;
    }

    @Scheduled(cron = "${cosmic.metrics-collector.scan-interval}")
    public void run() {
        final StopWatch stopWatch = new StopWatch(PublicIpAddressMetricsCollector.class.getSimpleName());

        stopWatch.start("Collecting public IP addresses metrics from the database");
        final List<Metric> metrics = publicIpAddressMetricsRepository.getMetrics();
        stopWatch.stop();

        stopWatch.start("Sending metrics to the message queue");
        metrics.forEach(metric -> {
            try {
                amqpTemplate.convertAndSend(
                        brokerExchange,
                        brokerExchangeKey,
                        metricWriter.writeValueAsString(metric)
                );
            } catch (final JsonProcessingException e) {
                throw new FailedToCollectMetricsException(e.getMessage(), e);
            }
        });
        stopWatch.stop();

        LOG.info(stopWatch.prettyPrint());
        LOG.info(String.format("Collected %d public IP addresses metrics.", metrics.size()));

    }

}
