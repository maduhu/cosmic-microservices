package com.github.missioncriticalcloud.cosmic.usage.core.model;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;

public class VirtualMachine extends Resource {

    private BigDecimal cpuAverage = BigDecimal.ZERO;
    private BigDecimal memoryAverage = BigDecimal.ZERO;

    public VirtualMachine() {
        // Empty constructor
    }

    public VirtualMachine(
            final String uuid,
            final BigDecimal sampleCount,
            final BigDecimal cpuAverage,
            final BigDecimal memoryAverage
    ) {
        setUuid(uuid);
        setSampleCount(sampleCount);
        setCpuAverage(cpuAverage);
        setMemoryAverage(memoryAverage);
    }

    public BigDecimal getCpuAverage() {
        return cpuAverage;
    }

    public void setCpuAverage(final BigDecimal cpuAverage) {
        this.cpuAverage = cpuAverage.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public BigDecimal getMemoryAverage() {
        return memoryAverage;
    }

    public void setMemoryAverage(final BigDecimal memoryAverage) {
        this.memoryAverage = memoryAverage.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

}
