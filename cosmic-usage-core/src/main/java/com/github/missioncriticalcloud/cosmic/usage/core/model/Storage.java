package com.github.missioncriticalcloud.cosmic.usage.core.model;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.missioncriticalcloud.cosmic.usage.core.views.DetailedView;
import com.github.missioncriticalcloud.cosmic.usage.core.views.GeneralView;

public class Storage {

    @JsonView(DetailedView.class)
    private List<Volume> volumes = new LinkedList<>();

    @JsonView({GeneralView.class, DetailedView.class})
    private BigDecimal total = BigDecimal.ZERO;

    public List<Volume> getVolumes() {
        return volumes;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void addTotal(final BigDecimal amountToAdd) {
        total = total.add(amountToAdd).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

}
