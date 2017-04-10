package com.github.missioncriticalcloud.cosmic.usage.core.model;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;

public class Compute {

    private BigDecimal cpu = BigDecimal.ZERO;
    private BigDecimal memory = BigDecimal.ZERO;

    public BigDecimal getCpu() {
        return cpu;
    }

    public void setCpu(final BigDecimal cpu) {
        this.cpu = cpu.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public void addCpu(final BigDecimal amountToAdd) {
        cpu = cpu.add(amountToAdd).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public BigDecimal getMemory() {
        return memory;
    }

    public void setMemory(final BigDecimal memory) {
        this.memory = memory.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public void addMemory(final BigDecimal amountToAdd) {
        memory = memory.add(amountToAdd).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

}