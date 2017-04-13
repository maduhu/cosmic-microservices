package com.github.missioncriticalcloud.cosmic.api.usage.utils;

import java.math.BigDecimal;

import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.SortBy;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.SortOrder;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Report;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SortingUtilsTest {

    @Test
    public void sortByDomainPath() {
        final Report report = new Report();

        Domain domain1 = new Domain("1");
        domain1.setPath("/bbb");
        report.getDomains().add(domain1);

        Domain domain2 = new Domain("2");
        domain2.setPath("/ccc");
        report.getDomains().add(domain2);

        Domain domain3 = new Domain("3");
        domain3.setPath("/aaa");
        report.getDomains().add(domain3);

        // Ascending
        SortingUtils.sort(report, SortBy.DOMAIN_PATH, SortOrder.ASC);
        Assertions.assertThat(report.getDomains().get(0)).isEqualTo(domain3);
        Assertions.assertThat(report.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(report.getDomains().get(2)).isEqualTo(domain2);

        // Descending
        SortingUtils.sort(report, SortBy.DOMAIN_PATH, SortOrder.DESC);
        Assertions.assertThat(report.getDomains().get(0)).isEqualTo(domain2);
        Assertions.assertThat(report.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(report.getDomains().get(2)).isEqualTo(domain3);
    }

    @Test
    public void sortByCpu() {
        final Report report = new Report();

        Domain domain1 = new Domain("1");
        domain1.getUsage().getCompute().getTotal().addCpu(BigDecimal.ONE);
        report.getDomains().add(domain1);

        Domain domain2 = new Domain("2");
        domain2.getUsage().getCompute().getTotal().addCpu(BigDecimal.TEN);
        report.getDomains().add(domain2);

        Domain domain3 = new Domain("3");
        domain3.getUsage().getCompute().getTotal().addCpu(BigDecimal.ZERO);
        report.getDomains().add(domain3);

        // Ascending
        SortingUtils.sort(report, SortBy.CPU, SortOrder.ASC);
        Assertions.assertThat(report.getDomains().get(0)).isEqualTo(domain3);
        Assertions.assertThat(report.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(report.getDomains().get(2)).isEqualTo(domain2);

        // Descending
        SortingUtils.sort(report, SortBy.CPU, SortOrder.DESC);
        Assertions.assertThat(report.getDomains().get(0)).isEqualTo(domain2);
        Assertions.assertThat(report.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(report.getDomains().get(2)).isEqualTo(domain3);
    }

    @Test
    public void sortByMemory() {
        final Report report = new Report();

        Domain domain1 = new Domain("1");
        domain1.getUsage().getCompute().getTotal().addMemory(BigDecimal.ONE);
        report.getDomains().add(domain1);

        Domain domain2 = new Domain("2");
        domain2.getUsage().getCompute().getTotal().addMemory(BigDecimal.TEN);
        report.getDomains().add(domain2);

        Domain domain3 = new Domain("3");
        domain3.getUsage().getCompute().getTotal().addMemory(BigDecimal.ZERO);
        report.getDomains().add(domain3);

        // Ascending
        SortingUtils.sort(report, SortBy.MEMORY, SortOrder.ASC);
        Assertions.assertThat(report.getDomains().get(0)).isEqualTo(domain3);
        Assertions.assertThat(report.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(report.getDomains().get(2)).isEqualTo(domain2);

        // Descending
        SortingUtils.sort(report, SortBy.MEMORY, SortOrder.DESC);
        Assertions.assertThat(report.getDomains().get(0)).isEqualTo(domain2);
        Assertions.assertThat(report.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(report.getDomains().get(2)).isEqualTo(domain3);
    }

    @Test
    public void sortByStorage() {
        final Report report = new Report();

        Domain domain1 = new Domain("1");
        domain1.getUsage().getStorage().addTotal(BigDecimal.ONE);
        report.getDomains().add(domain1);

        Domain domain2 = new Domain("2");
        domain2.getUsage().getStorage().addTotal(BigDecimal.TEN);
        report.getDomains().add(domain2);

        Domain domain3 = new Domain("3");
        domain3.getUsage().getStorage().addTotal(BigDecimal.ZERO);
        report.getDomains().add(domain3);

        // Ascending
        SortingUtils.sort(report, SortBy.VOLUME, SortOrder.ASC);
        Assertions.assertThat(report.getDomains().get(0)).isEqualTo(domain3);
        Assertions.assertThat(report.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(report.getDomains().get(2)).isEqualTo(domain2);

        // Descending
        SortingUtils.sort(report, SortBy.VOLUME, SortOrder.DESC);
        Assertions.assertThat(report.getDomains().get(0)).isEqualTo(domain2);
        Assertions.assertThat(report.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(report.getDomains().get(2)).isEqualTo(domain3);
    }

    @Test
    public void sortByPublicIp() {
        final Report report = new Report();

        Domain domain1 = new Domain("1");
        domain1.getUsage().getNetworking().getTotal().addPublicIps(BigDecimal.ONE);
        report.getDomains().add(domain1);

        Domain domain2 = new Domain("2");
        domain2.getUsage().getNetworking().getTotal().addPublicIps(BigDecimal.TEN);
        report.getDomains().add(domain2);

        Domain domain3 = new Domain("3");
        domain3.getUsage().getNetworking().getTotal().addPublicIps(BigDecimal.ZERO);
        report.getDomains().add(domain3);

        // Ascending
        SortingUtils.sort(report, SortBy.PUBLIC_IP, SortOrder.ASC);
        Assertions.assertThat(report.getDomains().get(0)).isEqualTo(domain3);
        Assertions.assertThat(report.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(report.getDomains().get(2)).isEqualTo(domain2);

        // Descending
        SortingUtils.sort(report, SortBy.PUBLIC_IP, SortOrder.DESC);
        Assertions.assertThat(report.getDomains().get(0)).isEqualTo(domain2);
        Assertions.assertThat(report.getDomains().get(1)).isEqualTo(domain1);
        Assertions.assertThat(report.getDomains().get(2)).isEqualTo(domain3);
    }

}
