package com.github.missioncriticalcloud.cosmic.usage.core.model;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.missioncriticalcloud.cosmic.usage.core.model.types.OsType;
import com.github.missioncriticalcloud.cosmic.usage.core.views.DetailedView;

public class VirtualMachine extends Resource {

    @JsonView(DetailedView.class)
    private String hostname;

    @JsonView(DetailedView.class)
    private OsType osType;

    @JsonView(DetailedView.class)
    private BigDecimal cpu = BigDecimal.ZERO;

    @JsonView(DetailedView.class)
    private BigDecimal memory = BigDecimal.ZERO;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    public OsType getOsType() {
        return osType;
    }

    public void setOsType(final OsType osType) {
        this.osType = osType;
    }

    public BigDecimal getCpu() {
        return cpu;
    }

    public void setCpu(final BigDecimal cpu) {
        this.cpu = cpu.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public BigDecimal getMemory() {
        return memory;
    }

    public void setMemory(final BigDecimal memory) {
        this.memory = memory.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

}
