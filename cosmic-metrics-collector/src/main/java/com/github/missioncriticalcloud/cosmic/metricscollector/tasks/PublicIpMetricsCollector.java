package com.github.missioncriticalcloud.cosmic.metricscollector.tasks;

import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.missioncriticalcloud.cosmic.metricscollector.exceptions.UnableToCollectMetricsException;
import com.github.missioncriticalcloud.cosmic.metricscollector.repositories.MetricsRepository;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Metric;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@ConditionalOnExpression("${cosmic.metrics-collector.enable-collectors}")
public class PublicIpMetricsCollector implements MetricsCollector {

    private static final Logger LOG = Logger.getLogger(PublicIpMetricsCollector.class.getName());

    private final MetricsRepository metricsRepository;
    private final AmqpTemplate amqpTemplate;
    private final ObjectWriter metricWriter;

    private final String brokerExchange;
    private final String brokerExchangeKey;

    @Autowired
    public PublicIpMetricsCollector(
            @Qualifier("publicIpMetricsRepository") final MetricsRepository metricsRepository,
            final AmqpTemplate amqpTemplate,
            final ObjectWriter metricWriter,
            @Value("${cosmic.metrics-collector.broker-exchange}") final String brokerExchange,
            @Value("${cosmic.metrics-collector.broker-exchange-key}") final String brokerExchangeKey
    ) {
        this.metricsRepository = metricsRepository;
        this.amqpTemplate = amqpTemplate;
        this.metricWriter = metricWriter;
        this.brokerExchange = brokerExchange;
        this.brokerExchangeKey = brokerExchangeKey;
    }

    @Override
    @Scheduled(cron = "${cosmic.metrics-collector.scan-interval}")
    public void run() {
        final StopWatch stopWatch = new StopWatch(PublicIpMetricsCollector.class.getSimpleName());

        stopWatch.start("Collecting public IP metrics from the database");
        final List<Metric> metrics = metricsRepository.getMetrics();
        stopWatch.stop();

        stopWatch.start("Sending public IP metrics to the message queue");
        metrics.forEach(metric -> {
            try {
                amqpTemplate.convertAndSend(
                        brokerExchange,
                        brokerExchangeKey,
                        metricWriter.writeValueAsString(metric)
                );
            } catch (final JsonProcessingException e) {
                throw new UnableToCollectMetricsException(e.getMessage(), e);
            }
        });
        stopWatch.stop();

        LOG.info(stopWatch.prettyPrint());
        LOG.info(String.format("Collected %d public IP metrics", metrics.size()));
    }

}
