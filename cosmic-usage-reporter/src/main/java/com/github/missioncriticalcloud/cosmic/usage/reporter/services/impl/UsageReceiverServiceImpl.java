package com.github.missioncriticalcloud.cosmic.usage.reporter.services.impl;

import java.util.logging.Logger;

import com.github.missioncriticalcloud.cosmic.usage.reporter.models.Quote;
import com.github.missioncriticalcloud.cosmic.usage.reporter.services.UsageReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UsageReceiverServiceImpl implements UsageReceiverService {

    private static final Logger LOG = Logger.getLogger(UsageReceiverServiceImpl.class.getName());

    RestTemplate restTemplate;

    @Autowired
    private void restTemplate(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void getQuote() {

        Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        LOG.info(quote.toString());
    }

    @Override
    public void getUsage() {
        getQuote();
    }
}