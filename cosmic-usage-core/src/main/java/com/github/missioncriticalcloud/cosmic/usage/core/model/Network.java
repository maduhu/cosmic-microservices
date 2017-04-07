package com.github.missioncriticalcloud.cosmic.usage.core.model;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;

public class Network {

    private BigDecimal publicIps = BigDecimal.ZERO;

    public BigDecimal getPublicIps() {
        return publicIps;
    }

    public void setPublicIps(final BigDecimal publicIps) {
        this.publicIps = publicIps.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public void addPublicIps(final BigDecimal amountToAdd) {
        publicIps = publicIps.add(amountToAdd).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

}
