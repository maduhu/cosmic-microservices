package com.github.missioncriticalcloud.cosmic.usage.core.model;

import java.util.ArrayList;
import java.util.List;

public class GeneralUsage {

    private List<Domain> domains = new ArrayList<>();

    public GeneralUsage() {
        // Empty constructor
    }

    public GeneralUsage(final List<Domain> domains) {
        this.domains = domains;
    }

    public List<Domain> getDomains() {
        return domains;
    }

}
