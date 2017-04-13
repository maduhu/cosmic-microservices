package com.github.missioncriticalcloud.cosmic.usage.core.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.missioncriticalcloud.cosmic.usage.core.views.DetailedView;
import com.github.missioncriticalcloud.cosmic.usage.core.views.GeneralView;

public class Report {

    @JsonView({GeneralView.class, DetailedView.class})
    private List<Domain> domains = new ArrayList<>();

    public Report() {
        // Empty constructor
    }

    public Report(final List<Domain> domains) {
        this.domains = domains;
    }

    public List<Domain> getDomains() {
        return domains;
    }

}
