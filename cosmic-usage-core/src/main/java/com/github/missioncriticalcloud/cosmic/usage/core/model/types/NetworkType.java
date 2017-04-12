package com.github.missioncriticalcloud.cosmic.usage.core.model.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NetworkType {

    VPC("VPC"),
    GUEST("Guest");

    private final String value;

    NetworkType(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
