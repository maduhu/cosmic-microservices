package com.github.missioncriticalcloud.cosmic.usage.core.model.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResourceType {

    VIRTUAL_MACHINE("VirtualMachine"),
    VOLUME("Volume"),
    PUBLIC_IP("PublicIp");

    private final String value;

    ResourceType(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
