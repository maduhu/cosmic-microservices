package com.github.missioncriticalcloud.cosmic.usage.core.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class FormatUtils {

    public static final DecimalFormat DECIMAL_FORMATTER = (DecimalFormat) DecimalFormat.getNumberInstance();
    public static final int DEFAULT_SCALE = 2;
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    public static final String DATE_FORMATTER_STRING = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern(DATE_FORMATTER_STRING);

    private FormatUtils() {
        // Empty constructor
    }

    static {
        DECIMAL_FORMATTER.applyPattern("0.00");
    }

}
