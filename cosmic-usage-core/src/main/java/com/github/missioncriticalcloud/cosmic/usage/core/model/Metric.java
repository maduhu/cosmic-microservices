package com.github.missioncriticalcloud.cosmic.usage.core.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

public class Metric {

    private String domainUuid;
    private String accountUuid;
    private String resourceUuid;

    private ResourceType resourceType;

    @JsonProperty("@timestamp")
    private DateTime timestamp;

    private final Map<String, Object> payload = new HashMap<>();

    public String getDomainUuid() {
        return domainUuid;
    }

    public void setDomainUuid(final String domainUuid) {
        this.domainUuid = domainUuid;
    }

    public String getAccountUuid() {
        return accountUuid;
    }

    public void setAccountUuid(final String accountUuid) {
        this.accountUuid = accountUuid;
    }

    public String getResourceUuid() {
        return resourceUuid;
    }

    public void setResourceUuid(final String resourceUuid) {
        this.resourceUuid = resourceUuid;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(final ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

}
