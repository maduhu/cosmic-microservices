package com.github.missioncriticalcloud.cosmic.api.usage.model;

import static com.github.missioncriticalcloud.cosmic.api.usage.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.api.usage.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class Domain {

    private String uuid;
    private BigDecimal sampleCount = BigDecimal.ZERO;

    private final List<Resource> resources = new LinkedList<>();

    public Domain() {}

    public Domain(final String uuid, final BigDecimal sampleCount) {
        setUuid(uuid);
        setSampleCount(sampleCount);
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
        this.sampleCount = sampleCount.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public List<Resource> getResources() {
        return resources;
    }

}
