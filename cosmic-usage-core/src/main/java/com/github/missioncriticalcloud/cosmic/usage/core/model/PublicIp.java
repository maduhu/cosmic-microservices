package com.github.missioncriticalcloud.cosmic.usage.core.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.missioncriticalcloud.cosmic.usage.core.views.DetailedView;

public class PublicIp extends Resource {

    @JsonView(DetailedView.class)
    private String value;

    @JsonView(DetailedView.class)
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
