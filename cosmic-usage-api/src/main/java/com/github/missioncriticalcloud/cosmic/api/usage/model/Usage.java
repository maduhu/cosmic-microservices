package com.github.missioncriticalcloud.cosmic.api.usage.model;

import java.math.BigDecimal;

public class Usage {

    private String aggregateId;
    private BigDecimal cpu = BigDecimal.ZERO;
    private BigDecimal memory = BigDecimal.ZERO;

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(final String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public BigDecimal getCpu() {
        return cpu;
    }

    public void setCpu(final BigDecimal cpu) {
        this.cpu = cpu;
    }

    public void addCpu(final BigDecimal amountToAdd) {
        cpu = cpu.add(amountToAdd);
    }

    public BigDecimal getMemory() {
        return memory;
    }

    public void setMemory(final BigDecimal memory) {
        this.memory = memory;
    }

    public void addMemory(final BigDecimal amountToAdd) {
        memory = memory.add(amountToAdd);
    }

}
