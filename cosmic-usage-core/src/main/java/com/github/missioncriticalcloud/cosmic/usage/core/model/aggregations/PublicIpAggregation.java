package com.github.missioncriticalcloud.cosmic.usage.core.model.aggregations;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;

public class PublicIpAggregation extends ResourceAggregation {

    private BigDecimal count = BigDecimal.ZERO;

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(final BigDecimal count) {
        this.count = count.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

}
