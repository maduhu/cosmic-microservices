package com.github.missioncriticalcloud.cosmic.usage.core.model;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.missioncriticalcloud.cosmic.usage.core.views.DetailedView;

public class Volume extends Resource {

    @JsonView(DetailedView.class)
    private String name;

    @JsonView(DetailedView.class)
    private BigDecimal size = BigDecimal.ZERO;

    @JsonView(DetailedView.class)
    private String attachedTo;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getSize() {
        return size.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public void setSize(final BigDecimal size) {
        this.size = size;
    }

    public void setAttachedTo(final String attachedTo) {
        this.attachedTo = attachedTo;
    }

    public String getAttachedTo() {
        return attachedTo;
    }

}
