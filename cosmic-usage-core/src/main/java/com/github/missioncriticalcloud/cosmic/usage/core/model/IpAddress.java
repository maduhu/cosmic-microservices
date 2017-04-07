package com.github.missioncriticalcloud.cosmic.usage.core.model;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;

public class IpAddress extends Resource {

    private BigDecimal ipAddressCount = BigDecimal.ZERO;

    public IpAddress() {
        // Empty constructor
    }

    public IpAddress(
            final String uuid,
            final BigDecimal sampleCount,
            final BigDecimal ipAddressCount
    ) {
        setUuid(uuid);
        setSampleCount(sampleCount);
        setIpAddressCount(ipAddressCount);
    }


    public BigDecimal getIpAddressCount() {
        return ipAddressCount;
    }

    public void setIpAddressCount(final BigDecimal ipAddressCount) {
        this.ipAddressCount = ipAddressCount.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

}
