package com.github.missioncriticalcloud.cosmic.usage.core.model;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class Compute {

    private List<VirtualMachine> virtualMachines = new LinkedList<>();
    private Total total = new Total();

    public List<VirtualMachine> getVirtualMachines() {
        return virtualMachines;
    }

    public Total getTotal() {
        return total;
    }

    public class Total {

        private BigDecimal cpu = BigDecimal.ZERO;
        private BigDecimal memory = BigDecimal.ZERO;

        public BigDecimal getCpu() {
            return cpu;
        }

        public void addCpu(final BigDecimal amountToAdd) {
            cpu = cpu.add(amountToAdd).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        }

        public BigDecimal getMemory() {
            return memory;
        }

        public void addMemory(final BigDecimal amountToAdd) {
            memory = memory.add(amountToAdd).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        }

    }

}
