package com.github.missioncriticalcloud.cosmic.api.usage.controllers;

import static com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.sort;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DATE_FORMATTER;

import java.util.List;

import com.github.missioncriticalcloud.cosmic.api.usage.services.SearchService;
import com.github.missioncriticalcloud.cosmic.api.usage.services.UsageService;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.SortBy;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.SortOrder;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Usage;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class MainController {

    private final SearchService searchService;
    private final UsageService usageService;

    @Autowired
    public MainController(final SearchService searchService, final UsageService usageService) {
        this.searchService = searchService;
        this.usageService = usageService;
    }

    @RequestMapping("/")
    public List<Usage> defaultEndpoint(
            @RequestParam("from") final String fromAsString,
            @RequestParam("to") final String toAsString,
            @RequestParam("path") final String path,
            @RequestParam(value = "sortBy", required = false, defaultValue = "DOMAIN_PATH") final SortBy sortBy,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "ASC") final SortOrder sortOrder
    ) {
        final DateTime from = DATE_FORMATTER.parseDateTime(fromAsString);
        final DateTime to = DATE_FORMATTER.parseDateTime(toAsString);

        final List<Domain> domains = searchService.search(from, to, path);
        final List<Usage> domainsUsage = usageService.calculateDomainsUsage(domains, from, to);
        sort(domainsUsage, sortBy, sortOrder);

        return domainsUsage;
    }

}
