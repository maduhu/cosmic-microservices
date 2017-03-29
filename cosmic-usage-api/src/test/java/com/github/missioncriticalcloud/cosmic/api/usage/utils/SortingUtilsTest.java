package com.github.missioncriticalcloud.cosmic.api.usage.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.github.missioncriticalcloud.cosmic.api.usage.model.Domain;
import com.github.missioncriticalcloud.cosmic.api.usage.model.Usage;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.SortBy;
import com.github.missioncriticalcloud.cosmic.api.usage.utils.SortingUtils.SortOrder;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SortingUtilsTest {

    @Test
    public void sortByDomainPath() {
        final List<Usage> domainsUsage = new ArrayList<>();

        Domain domain1 = new Domain();
        domain1.setPath("/bbb");
        Usage usage1 = new Usage(domain1, BigDecimal.ZERO, BigDecimal.ZERO);
        domainsUsage.add(usage1);

        Domain domain2 = new Domain();
        domain2.setPath("/ccc");
        Usage usage2 = new Usage(domain2, BigDecimal.ZERO, BigDecimal.ZERO);
        domainsUsage.add(usage2);

        Domain domain3 = new Domain();
        domain3.setPath("/aaa");
        Usage usage3 = new Usage(domain3, BigDecimal.ZERO, BigDecimal.ZERO);
        domainsUsage.add(usage3);

        // Ascending
        SortingUtils.sort(domainsUsage, SortBy.DOMAIN_PATH, SortOrder.ASC);
        Assertions.assertThat(domainsUsage.get(0)).isEqualTo(usage3);
        Assertions.assertThat(domainsUsage.get(1)).isEqualTo(usage1);
        Assertions.assertThat(domainsUsage.get(2)).isEqualTo(usage2);

        // Descending
        SortingUtils.sort(domainsUsage, SortBy.DOMAIN_PATH, SortOrder.DESC);
        Assertions.assertThat(domainsUsage.get(0)).isEqualTo(usage2);
        Assertions.assertThat(domainsUsage.get(1)).isEqualTo(usage1);
        Assertions.assertThat(domainsUsage.get(2)).isEqualTo(usage3);
    }

    @Test
    public void sortByCpu() {
        final List<Usage> domainsUsage = new ArrayList<>();

        Usage usage1 = new Usage(new Domain(), BigDecimal.ONE, BigDecimal.ZERO);
        domainsUsage.add(usage1);

        Usage usage2 = new Usage(new Domain(), BigDecimal.TEN, BigDecimal.ZERO);
        domainsUsage.add(usage2);

        Usage usage3 = new Usage(new Domain(), BigDecimal.ZERO, BigDecimal.ZERO);
        domainsUsage.add(usage3);

        // Ascending
        SortingUtils.sort(domainsUsage, SortBy.CPU, SortOrder.ASC);
        Assertions.assertThat(domainsUsage.get(0)).isEqualTo(usage3);
        Assertions.assertThat(domainsUsage.get(1)).isEqualTo(usage1);
        Assertions.assertThat(domainsUsage.get(2)).isEqualTo(usage2);

        // Descending
        SortingUtils.sort(domainsUsage, SortBy.CPU, SortOrder.DESC);
        Assertions.assertThat(domainsUsage.get(0)).isEqualTo(usage2);
        Assertions.assertThat(domainsUsage.get(1)).isEqualTo(usage1);
        Assertions.assertThat(domainsUsage.get(2)).isEqualTo(usage3);
    }

    @Test
    public void sortByMemory() {
        final List<Usage> domainsUsage = new ArrayList<>();

        Usage usage1 = new Usage(new Domain(), BigDecimal.ZERO, BigDecimal.ONE);
        domainsUsage.add(usage1);

        Usage usage2 = new Usage(new Domain(), BigDecimal.ZERO, BigDecimal.TEN);
        domainsUsage.add(usage2);

        Usage usage3 = new Usage(new Domain(), BigDecimal.ZERO, BigDecimal.ZERO);
        domainsUsage.add(usage3);

        // Ascending
        SortingUtils.sort(domainsUsage, SortBy.MEMORY, SortOrder.ASC);
        Assertions.assertThat(domainsUsage.get(0)).isEqualTo(usage3);
        Assertions.assertThat(domainsUsage.get(1)).isEqualTo(usage1);
        Assertions.assertThat(domainsUsage.get(2)).isEqualTo(usage2);

        // Descending
        SortingUtils.sort(domainsUsage, SortBy.MEMORY, SortOrder.DESC);
        Assertions.assertThat(domainsUsage.get(0)).isEqualTo(usage2);
        Assertions.assertThat(domainsUsage.get(1)).isEqualTo(usage1);
        Assertions.assertThat(domainsUsage.get(2)).isEqualTo(usage3);
    }

}
