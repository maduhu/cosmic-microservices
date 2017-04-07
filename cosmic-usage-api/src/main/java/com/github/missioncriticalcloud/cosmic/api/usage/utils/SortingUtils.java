package com.github.missioncriticalcloud.cosmic.api.usage.utils;

import com.github.missioncriticalcloud.cosmic.usage.core.model.GeneralUsage;
import com.github.missioncriticalcloud.cosmic.usage.core.model.Usage;

public class SortingUtils {

    public enum SortBy {
        DOMAIN_PATH, CPU, MEMORY, STORAGE, IP_ADDRESS
    }

    public enum SortOrder {
        ASC, DESC
    }

    private SortingUtils() {
        // Empty constructor
    }

    public static void sort(final GeneralUsage generalUsage, final SortBy sortBy, final SortOrder sortOrder) {
        generalUsage.getDomains().sort((domain1, domain2) -> {
            final Usage usage1 = domain1.getUsage();
            final Usage usage2 = domain2.getUsage();

            switch (sortBy) {
                case DOMAIN_PATH:
                    return (SortOrder.DESC.equals(sortOrder))
                            ? domain2.getPath().compareToIgnoreCase(domain1.getPath())
                            : domain1.getPath().compareToIgnoreCase(domain2.getPath());
                case CPU:
                    return (SortOrder.DESC.equals(sortOrder))
                            ? usage2.getCompute().getCpu().compareTo(usage1.getCompute().getCpu())
                            : usage1.getCompute().getCpu().compareTo(usage2.getCompute().getCpu());
                case MEMORY:
                    return (SortOrder.DESC.equals(sortOrder))
                            ? usage2.getCompute().getMemory().compareTo(usage1.getCompute().getMemory())
                            : usage1.getCompute().getMemory().compareTo(usage2.getCompute().getMemory());
                case STORAGE:
                    return (SortOrder.DESC.equals(sortOrder))
                            ? usage2.getStorage().compareTo(usage1.getStorage())
                            : usage1.getStorage().compareTo(usage2.getStorage());
                case IP_ADDRESS:
                    return (SortOrder.DESC.equals(sortOrder))
                            ? usage2.getNetwork().getPublicIps().compareTo(usage1.getNetwork().getPublicIps())
                            : usage1.getNetwork().getPublicIps().compareTo(usage2.getNetwork().getPublicIps());
                default:
                    return 0;
            }
        });
    }

}
