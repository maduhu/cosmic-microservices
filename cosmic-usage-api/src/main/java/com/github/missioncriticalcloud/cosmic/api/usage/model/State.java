package com.github.missioncriticalcloud.cosmic.api.usage.model;

import java.math.BigDecimal;

public class State {

    public static final String RUNNING = "Running";
    public static final String STOPPED = "Stopped";

    private String value;
    private BigDecimal sampleCount = BigDecimal.ZERO;
    private BigDecimal cpuAverage = BigDecimal.ZERO;
    private BigDecimal memoryAverage = BigDecimal.ZERO;

    public State() {}

    public State(
            final String value,
            final BigDecimal sampleCount,
            final BigDecimal cpuAverage,
            final BigDecimal memoryAverage
    ) {
        this.value = value;
        this.sampleCount = sampleCount;
        this.cpuAverage = cpuAverage;
        this.memoryAverage = memoryAverage;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public BigDecimal getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(final BigDecimal sampleCount) {
        this.sampleCount = sampleCount;
    }

    public BigDecimal getCpuAverage() {
        return cpuAverage;
    }

    public void setCpuAverage(final BigDecimal cpuAverage) {
        this.cpuAverage = cpuAverage;
    }

    public BigDecimal getMemoryAverage() {
        return memoryAverage;
    }

    public void setMemoryAverage(final BigDecimal memoryAverage) {
        this.memoryAverage = memoryAverage;
    }

}
