package com.github.missioncriticalcloud.cosmic.usage.core.model;

import java.math.BigDecimal;

public class Usage {

    private Compute compute = new Compute();
    private Storage storage = new Storage();
    private Networking networking = new Networking();

    public Compute getCompute() {
        return compute;
    }

    public Storage getStorage() {
        return storage;
    }

    public Networking getNetworking() {
        return networking;
    }

    public boolean isEmpty() {
        return compute.getTotal().getCpu().compareTo(BigDecimal.ZERO) == 0 &&
                compute.getTotal().getMemory().compareTo(BigDecimal.ZERO) == 0 &&
                storage.getTotal().compareTo(BigDecimal.ZERO) == 0 &&
                networking.getTotal().getPublicIps().compareTo(BigDecimal.ZERO) == 0;
    }

}
