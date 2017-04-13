package com.github.missioncriticalcloud.cosmic.usage.core.model.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OsType {

    WINDOWS("Windows"),
    RED_HAT("RedHat"),
    OPEN_SOURCE("OpenSource");

    private final String value;

    OsType(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
