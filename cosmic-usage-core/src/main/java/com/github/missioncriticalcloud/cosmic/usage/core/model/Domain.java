package com.github.missioncriticalcloud.cosmic.usage.core.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Domain {

    private String uuid;
    private String name;
    private String path;
    private Usage usage = new Usage();

    @JsonIgnore
    private final List<VirtualMachine> virtualMachines = new LinkedList<>();

    @JsonIgnore
    private final List<Storage> storageItems = new LinkedList<>();

    @JsonIgnore
    private final List<IpAddress> ipAddresses = new LinkedList<>();

    public Domain() {
        // Empty constructor
    }

    public Domain(final String uuid) {
        setUuid(uuid);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public Usage getUsage() {
        return usage;
    }

    public List<VirtualMachine> getVirtualMachines() {
        return virtualMachines;
    }

    public List<Storage> getStorageItems() {
        return storageItems;
    }

    public List<IpAddress> getIpAddresses() {
        return ipAddresses;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Domain)) {
            return false;
        }
        final Domain domain = (Domain) o;
        return Objects.equals(getUuid(), domain.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

}
