package com.github.missioncriticalcloud.cosmic.usage.core.model;

import java.util.LinkedList;
import java.util.List;

import com.github.missioncriticalcloud.cosmic.usage.core.model.types.NetworkType;

public class Network extends Resource {

    private String name;
    private NetworkType type;
    private List<PublicIp> publicIps = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public NetworkType getType() {
        return type;
    }

    public void setType(final NetworkType type) {
        this.type = type;
    }

    public List<PublicIp> getPublicIps() {
        return publicIps;
    }

}
