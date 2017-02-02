package com.github.missioncriticalcloud.cosmic.api.usage.model;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class Domain {

    private String uuid;
    private BigDecimal sampleCount = BigDecimal.ZERO;

    private final List<Resource> resources = new LinkedList<>();

    public Domain() {}

    public Domain(final String uuid, final BigDecimal sampleCount) {
        this.uuid = uuid;
        this.sampleCount = sampleCount;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(final BigDecimal sampleCount) {
        this.sampleCount = sampleCount;
    }

    public List<Resource> getResources() {
        return resources;
    }

}
