package com.github.missioncriticalcloud.cosmic.usage.core.model;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;

public class Usage {

    private Compute compute = new Compute();
    private BigDecimal storage = BigDecimal.ZERO;
    private Network network = new Network();

    public Usage() {
        // Empty constructor
    }

    public Compute getCompute() {
        return compute;
    }

    public BigDecimal getStorage() {
        return storage;
    }

    public void setStorage(final BigDecimal storage) {
        this.storage = storage.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public void addStorage(final BigDecimal amountToAdd) {
        storage = storage.add(amountToAdd).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public Network getNetwork() {
        return network;
    }

}
