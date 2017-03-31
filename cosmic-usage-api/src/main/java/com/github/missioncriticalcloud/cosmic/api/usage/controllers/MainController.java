package com.github.missioncriticalcloud.cosmic.api.usage.controllers;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DATE_FORMATTER;

import com.github.missioncriticalcloud.cosmic.api.usage.services.UsageService;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.SortBy;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.SortOrder;
import com.github.missioncriticalcloud.cosmic.usage.core.model.GeneralUsage;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class MainController {

    private final UsageService usageService;

    @Autowired
    public MainController(final UsageService usageService) {
        this.usageService = usageService;
    }

    @RequestMapping("/general")
    public GeneralUsage general(
            @RequestParam("from") final String fromAsString,
            @RequestParam("to") final String toAsString,
            @RequestParam("path") final String path,
            @RequestParam(value = "sortBy", required = false, defaultValue = "DOMAIN_PATH") final SortBy sortBy,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "ASC") final SortOrder sortOrder
    ) {
        final DateTime from = DATE_FORMATTER.parseDateTime(fromAsString);
        final DateTime to = DATE_FORMATTER.parseDateTime(toAsString);

        final GeneralUsage generalUsage = usageService.calculateGeneralUsage(from, to, path);
        SortingUtils.sort(generalUsage, sortBy, sortOrder);

        return generalUsage;
    }

}
