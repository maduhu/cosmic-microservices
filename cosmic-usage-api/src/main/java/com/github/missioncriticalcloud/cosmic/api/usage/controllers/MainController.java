package com.github.missioncriticalcloud.cosmic.api.usage.controllers;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_DATE_FORMAT;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.missioncriticalcloud.cosmic.api.usage.services.UsageCalculator;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.SortBy;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.SortOrder;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Report;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Unit;
import com.github.missioncriticalcloud.cosmic.usage.core.views.DetailedView;
import com.github.missioncriticalcloud.cosmic.usage.core.views.GeneralView;
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

    private final UsageCalculator usageCalculator;

    @Autowired
    public MainController(final UsageCalculator usageCalculator) {
        this.usageCalculator = usageCalculator;
    }

    @RequestMapping("/general")
    @JsonView(GeneralView.class)
    public Report general(
            @RequestParam @DateTimeFormat(pattern = DEFAULT_DATE_FORMAT) final DateTime from,
            @RequestParam @DateTimeFormat(pattern = DEFAULT_DATE_FORMAT) final DateTime to,
            @RequestParam final String path,
            @RequestParam(required = false, defaultValue = SortBy.DEFAULT) final SortBy sortBy,
            @RequestParam(required = false, defaultValue = SortOrder.DEFAULT) final SortOrder sortOrder,
            @RequestParam(required = false, defaultValue = Unit.DEFAULT) final Unit unit
    ) {
        final Report report = usageCalculator.calculate(from, to, path, unit, false);
        SortingUtils.sort(report, sortBy, sortOrder);

        return report;
    }

    @RequestMapping("/detailed")
    @JsonView(DetailedView.class)
    public Report detailed(
            @RequestParam @DateTimeFormat(pattern = DEFAULT_DATE_FORMAT) final DateTime from,
            @RequestParam @DateTimeFormat(pattern = DEFAULT_DATE_FORMAT) final DateTime to,
            @RequestParam final String path,
            @RequestParam(required = false, defaultValue = SortBy.DEFAULT) final SortBy sortBy,
            @RequestParam(required = false, defaultValue = SortOrder.DEFAULT) final SortOrder sortOrder,
            @RequestParam(required = false, defaultValue = Unit.DEFAULT) final Unit unit
    ) {
        final Report report = usageCalculator.calculate(from, to, path, unit, true);
        SortingUtils.sort(report, sortBy, sortOrder);

        return report;
    }

}
