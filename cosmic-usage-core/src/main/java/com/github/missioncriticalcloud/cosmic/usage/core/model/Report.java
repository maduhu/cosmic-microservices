package com.github.missioncriticalcloud.cosmic.usage.core.model;

import java.util.ArrayList;
import java.util.List;

public class Report {

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
