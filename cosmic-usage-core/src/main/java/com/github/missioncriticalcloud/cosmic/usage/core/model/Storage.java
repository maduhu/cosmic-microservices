package com.github.missioncriticalcloud.cosmic.usage.core.model;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;

public class Storage extends Resource {

    private BigDecimal storageSum = BigDecimal.ZERO;

    public Storage() {
        // Empty constructor
    }

    public Storage(
            final String uuid,
            final BigDecimal sampleCount,
            final BigDecimal storageSum
    ) {
        setUuid(uuid);
        setSampleCount(sampleCount);
        setStorageSum(storageSum);
    }


    public BigDecimal getStorageSum() {
        return storageSum;
    }

    public void setStorageSum(final BigDecimal storageSum) {
        this.storageSum = storageSum.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

}
