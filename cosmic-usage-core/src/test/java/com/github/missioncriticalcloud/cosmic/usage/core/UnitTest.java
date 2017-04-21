package com.github.missioncriticalcloud.cosmic.usage.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Unit;
import org.junit.Test;

public class UnitTest {

    @Test
    public void testConvert() {
        final BigDecimal tenGiga = BigDecimal.valueOf(10737418240L);

        assertThat(Unit.BYTES.convert(tenGiga))
                .isEqualByComparingTo(BigDecimal.valueOf(10737418240L));

        assertThat(Unit.KB.convert(tenGiga))
                .isEqualByComparingTo(BigDecimal.valueOf(10485760L));

        assertThat(Unit.MB.convert(tenGiga))
                .isEqualByComparingTo(BigDecimal.valueOf(10240L));

        assertThat(Unit.GB.convert(tenGiga))
                .isEqualByComparingTo(BigDecimal.valueOf(10L));
    }

}
