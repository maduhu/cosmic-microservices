package com.github.missioncriticalcloud.cosmic.api.usage.controllers;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DATE_FORMATTER_STRING;

import com.github.missioncriticalcloud.cosmic.api.usage.services.UsageService;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.SortBy;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.SortOrder;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Report;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    public Report general(
            @RequestParam @DateTimeFormat(pattern = DATE_FORMATTER_STRING) final DateTime from,
            @RequestParam @DateTimeFormat(pattern = DATE_FORMATTER_STRING) final DateTime to,
            @RequestParam final String path,
            @RequestParam(required = false, defaultValue = SortBy.DEFAULT) final SortBy sortBy,
            @RequestParam(required = false, defaultValue = SortOrder.DEFAULT) final SortOrder sortOrder
    ) {
        final Report report = usageService.calculateGeneralUsage(from, to, path);
        SortingUtils.sort(report, sortBy, sortOrder);

        return report;
    }

}
