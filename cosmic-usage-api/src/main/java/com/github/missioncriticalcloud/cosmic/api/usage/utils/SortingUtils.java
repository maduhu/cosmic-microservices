package com.github.missioncriticalcloud.cosmic.api.usage.utils;

import java.util.List;

import com.github.missioncriticalcloud.cosmic.api.usage.model.Usage;

public class SortingUtils {

    public enum SortBy {
        DOMAIN_PATH, CPU, MEMORY
    }

    public enum SortOrder {
        ASC, DESC
    }

    private SortingUtils() {
        // Empty constructor
    }

    public static void sort(final List<Usage> domainsUsage, final SortBy sortBy, final SortOrder sortOrder) {
        domainsUsage.sort((usage1, usage2) -> {
            switch (sortBy) {
                case DOMAIN_PATH:
                    return (SortOrder.DESC.equals(sortOrder))
                            ? usage2.getDomain().getPath().compareToIgnoreCase(usage1.getDomain().getPath())
                            : usage1.getDomain().getPath().compareToIgnoreCase(usage2.getDomain().getPath());
                case CPU:
                    return (SortOrder.DESC.equals(sortOrder))
                            ? usage2.getCpu().compareTo(usage1.getCpu())
                            : usage1.getCpu().compareTo(usage2.getCpu());
                case MEMORY:
                    return (SortOrder.DESC.equals(sortOrder))
                            ? usage2.getMemory().compareTo(usage1.getMemory())
                            : usage1.getMemory().compareTo(usage2.getMemory());
                default:
                    return 0;
            }
        });
    }

}
