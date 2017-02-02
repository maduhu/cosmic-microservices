package com.github.missioncriticalcloud.cosmic.api.usage.utils;

import static com.github.missioncriticalcloud.cosmic.api.usage.utils.FormatUtils.DECIMAL_FORMATTER;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(final BigDecimal bigDecimal, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(DECIMAL_FORMATTER.format(bigDecimal));
    }

}
