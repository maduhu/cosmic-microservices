package com.github.missioncriticalcloud.cosmic.usage.core.model.aggregations;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class DomainAggregation extends ResourceAggregation {

    private final List<VirtualMachineAggregation> virtualMachineAggregations = new LinkedList<>();
    private final List<VolumeAggregation> volumeAggregations = new LinkedList<>();
    private final List<PublicIpAggregation> publicIpAggregations = new LinkedList<>();

    public DomainAggregation(final String uuid) {
        setUuid(uuid);
    }

    public List<VirtualMachineAggregation> getVirtualMachineAggregations() {
        return virtualMachineAggregations;
    }

    public List<VolumeAggregation> getVolumeAggregations() {
        return volumeAggregations;
    }

    public List<PublicIpAggregation> getPublicIpAggregations() {
        return publicIpAggregations;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof DomainAggregation)) {
            return false;
        }

        final DomainAggregation domainAggregation = (DomainAggregation) o;

        return Objects.equals(getUuid(), domainAggregation.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

}
