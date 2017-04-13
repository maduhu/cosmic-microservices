package com.github.missioncriticalcloud.cosmic.usage.core.model.aggregations;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;

public class VirtualMachineAggregation extends ResourceAggregation {

    private BigDecimal cpuAverage = BigDecimal.ZERO;
    private BigDecimal memoryAverage = BigDecimal.ZERO;

    public BigDecimal getCpuAverage() {
        return cpuAverage.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public void setCpuAverage(final BigDecimal cpuAverage) {
        this.cpuAverage = cpuAverage;
    }

    public BigDecimal getMemoryAverage() {
        return memoryAverage.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public void setMemoryAverage(final BigDecimal memoryAverage) {
        this.memoryAverage = memoryAverage;
    }

}
