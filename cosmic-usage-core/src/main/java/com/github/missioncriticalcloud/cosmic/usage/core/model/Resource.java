package com.github.missioncriticalcloud.cosmic.usage.core.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.missioncriticalcloud.cosmic.usage.core.views.DetailedView;

public abstract class Resource {

    @JsonView(DetailedView.class)
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

}
