package com.github.missioncriticalcloud.cosmic.api.usage.utils;

import java.text.DecimalFormat;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class FormatUtils {

    public static final DecimalFormat DECIMAL_FORMATTER = (DecimalFormat) DecimalFormat.getNumberInstance();
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");

    static {
        DECIMAL_FORMATTER.applyPattern("0.00");
    }

}
