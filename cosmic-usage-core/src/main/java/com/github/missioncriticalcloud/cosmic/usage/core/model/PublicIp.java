package com.github.missioncriticalcloud.cosmic.usage.core.model;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.missioncriticalcloud.cosmic.usage.core.views.DetailedView;

public class PublicIp extends Resource {

    @JsonView(DetailedView.class)
    private String value;

    @JsonView(DetailedView.class)
    private State state;

    @JsonView(DetailedView.class)
    private BigDecimal amount;

    @JsonIgnore
    private Network network;

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

    public BigDecimal getAmount() {
        return amount.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(final Network network) {
        this.network = network;
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
