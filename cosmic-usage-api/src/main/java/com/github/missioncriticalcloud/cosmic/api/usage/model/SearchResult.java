package com.github.missioncriticalcloud.cosmic.api.usage.model;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class SearchResult {

    private BigDecimal sampleCount = BigDecimal.ZERO;

    private final List<Domain> domains = new LinkedList<>();

    public SearchResult() {}

    public SearchResult(final BigDecimal sampleCount) {
        this.sampleCount = sampleCount;
    }

    public BigDecimal getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(final BigDecimal sampleCount) {
        this.sampleCount = sampleCount;
    }

    public List<Domain> getDomains() {
        return domains;
    }

}
