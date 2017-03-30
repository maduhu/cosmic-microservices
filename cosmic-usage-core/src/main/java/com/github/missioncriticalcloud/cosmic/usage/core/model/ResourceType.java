package com.github.missioncriticalcloud.cosmic.usage.core.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResourceType {

    VIRTUAL_MACHINE("VirtualMachine"),
    PUBLIC_IP_ADDRESS("PublicIpAddress"),
    STORAGE("Storage");

    private final String value;

    ResourceType(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
