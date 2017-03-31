package com.github.missioncriticalcloud.cosmic.api.usage.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.SortBy;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.SortOrder;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import com.github.missioncriticalcloud.cosmic.usage.core.model.GeneralUsage;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Usage;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SortingUtilsTest {

    @Test
    public void sortByDomainPath() {
        final GeneralUsage generalUsage = new GeneralUsage();

        Domain domain1 = new Domain();
        domain1.setPath("/bbb");
        generalUsage.getDomains().add(domain1);

        Domain domain2 = new Domain();
        domain2.setPath("/ccc");
        generalUsage.getDomains().add(domain2);

        Domain domain3 = new Domain();
        domain3.setPath("/aaa");
        generalUsage.getDomains().add(domain3);

        // Ascending
        SortingUtils.sort(generalUsage, SortBy.DOMAIN_PATH, SortOrder.ASC);
        Assertions.assertThat(generalUsage.getDomains().get(0)).isEqualTo(domain3);
        Assertions.assertThat(generalUsage.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(generalUsage.getDomains().get(2)).isEqualTo(domain2);

        // Descending
        SortingUtils.sort(generalUsage, SortBy.DOMAIN_PATH, SortOrder.DESC);
        Assertions.assertThat(generalUsage.getDomains().get(0)).isEqualTo(domain2);
        Assertions.assertThat(generalUsage.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(generalUsage.getDomains().get(2)).isEqualTo(domain3);
    }

    @Test
    public void sortByCpu() {
        final GeneralUsage generalUsage = new GeneralUsage();

        Domain domain1 = new Domain();
        domain1.getUsage().getCompute().setCpu(BigDecimal.ONE);
        generalUsage.getDomains().add(domain1);

        Domain domain2 = new Domain();
        domain1.getUsage().getCompute().setCpu(BigDecimal.TEN);
        generalUsage.getDomains().add(domain2);

        Domain domain3 = new Domain();
        domain1.getUsage().getCompute().setCpu(BigDecimal.ZERO);
        generalUsage.getDomains().add(domain3);

        // Ascending
        SortingUtils.sort(generalUsage, SortBy.CPU, SortOrder.ASC);
        Assertions.assertThat(generalUsage.getDomains().get(0)).isEqualTo(domain3);
        Assertions.assertThat(generalUsage.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(generalUsage.getDomains().get(2)).isEqualTo(domain2);

        // Descending
        SortingUtils.sort(generalUsage, SortBy.CPU, SortOrder.DESC);
        Assertions.assertThat(generalUsage.getDomains().get(0)).isEqualTo(domain2);
        Assertions.assertThat(generalUsage.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(generalUsage.getDomains().get(2)).isEqualTo(domain3);
    }

    @Test
    public void sortByMemory() {
        final GeneralUsage generalUsage = new GeneralUsage();

        Domain domain1 = new Domain();
        domain1.getUsage().getCompute().setMemory(BigDecimal.ONE);
        generalUsage.getDomains().add(domain1);

        Domain domain2 = new Domain();
        domain1.getUsage().getCompute().setMemory(BigDecimal.TEN);
        generalUsage.getDomains().add(domain2);

        Domain domain3 = new Domain();
        domain1.getUsage().getCompute().setMemory(BigDecimal.ZERO);
        generalUsage.getDomains().add(domain3);

        // Ascending
        SortingUtils.sort(generalUsage, SortBy.MEMORY, SortOrder.ASC);
        Assertions.assertThat(generalUsage.getDomains().get(0)).isEqualTo(domain3);
        Assertions.assertThat(generalUsage.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(generalUsage.getDomains().get(2)).isEqualTo(domain2);

        // Descending
        SortingUtils.sort(generalUsage, SortBy.MEMORY, SortOrder.DESC);
        Assertions.assertThat(generalUsage.getDomains().get(0)).isEqualTo(domain2);
        Assertions.assertThat(generalUsage.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(generalUsage.getDomains().get(2)).isEqualTo(domain3);
    }

    @Test
    public void sortByStorage() {
        final GeneralUsage generalUsage = new GeneralUsage();

        Domain domain1 = new Domain();
        domain1.getUsage().setStorage(BigDecimal.ONE);
        generalUsage.getDomains().add(domain1);

        Domain domain2 = new Domain();
        domain1.getUsage().setStorage(BigDecimal.TEN);
        generalUsage.getDomains().add(domain2);

        Domain domain3 = new Domain();
        domain1.getUsage().setStorage(BigDecimal.ZERO);
        generalUsage.getDomains().add(domain3);

        // Ascending
        SortingUtils.sort(generalUsage, SortBy.STORAGE, SortOrder.ASC);
        Assertions.assertThat(generalUsage.getDomains().get(0)).isEqualTo(domain3);
        Assertions.assertThat(generalUsage.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(generalUsage.getDomains().get(2)).isEqualTo(domain2);

        // Descending
        SortingUtils.sort(generalUsage, SortBy.STORAGE, SortOrder.DESC);
        Assertions.assertThat(generalUsage.getDomains().get(0)).isEqualTo(domain2);
        Assertions.assertThat(generalUsage.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(generalUsage.getDomains().get(2)).isEqualTo(domain3);
    }

    @Test
    public void sortByIpAddress() {
        final GeneralUsage generalUsage = new GeneralUsage();

        Domain domain1 = new Domain();
        domain1.getUsage().getNetwork().setPublicIps(BigDecimal.ONE);
        generalUsage.getDomains().add(domain1);

        Domain domain2 = new Domain();
        domain1.getUsage().getNetwork().setPublicIps(BigDecimal.TEN);
        generalUsage.getDomains().add(domain2);

        Domain domain3 = new Domain();
        domain1.getUsage().getNetwork().setPublicIps(BigDecimal.ZERO);
        generalUsage.getDomains().add(domain3);

        // Ascending
        SortingUtils.sort(generalUsage, SortBy.IP_ADDRESS, SortOrder.ASC);
        Assertions.assertThat(generalUsage.getDomains().get(0)).isEqualTo(domain3);
        Assertions.assertThat(generalUsage.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(generalUsage.getDomains().get(2)).isEqualTo(domain2);

        // Descending
        SortingUtils.sort(generalUsage, SortBy.IP_ADDRESS, SortOrder.DESC);
        Assertions.assertThat(generalUsage.getDomains().get(0)).isEqualTo(domain2);
        Assertions.assertThat(generalUsage.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(generalUsage.getDomains().get(2)).isEqualTo(domain3);
    }

}
