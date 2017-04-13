package com.github.missioncriticalcloud.cosmic.usage.core.model;

import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_ROUNDING_MODE;
import static com.github.missioncriticalcloud.cosmic.usage.core.utils.FormatUtils.DEFAULT_SCALE;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class Networking {

    private List<Network> networks = new LinkedList<>();
    private Total total = new Total();

    public List<Network> getNetworks() {
        return networks;
    }

    public Total getTotal() {
        return total;
    }

    public class Total {

        private BigDecimal publicIps = BigDecimal.ZERO;

        public BigDecimal getPublicIps() {
            return publicIps;
        }

        public void addPublicIps(final BigDecimal amountToAdd) {
            publicIps = publicIps.add(amountToAdd).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        }

    }

}
