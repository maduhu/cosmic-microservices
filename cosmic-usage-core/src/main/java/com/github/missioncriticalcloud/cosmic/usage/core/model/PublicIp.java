package com.github.missioncriticalcloud.cosmic.usage.core.model;

import com.fasterxml.jackson.annotation.JsonValue;

public class PublicIp extends Resource {

    private String value;
    private State state;

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public enum State {

        ATTACHED("Attached"),
        DETACHED("Detached");

        private final String value;

        State(final String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

    }

}
