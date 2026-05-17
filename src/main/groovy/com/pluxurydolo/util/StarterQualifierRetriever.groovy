package com.pluxurydolo.util

import org.gradle.api.logging.Logger

import java.util.stream.Collectors

import static java.util.Arrays.stream

class StarterQualifierRetriever {
    static String retrieve(String projectName, Logger logger) {
        String[] qualifierParts = projectName.split('-')
        String starterQualifier = starterQualifier(qualifierParts)

        logger.lifecycle("lugw [logs-plugin] Название стартера: $starterQualifier")

        return starterQualifier
    }

    private static String starterQualifier(String[] qualifierParts) {
        return stream(qualifierParts)
                .filter { part -> part != 'spring' && part != 'boot' }
                .collect(Collectors.joining('-'))
    }
}
