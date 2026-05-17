package com.pluxurydolo.util

import org.gradle.api.logging.Logger

class StarterQualifierRetriever {
    static String retrieve(String projectName, Logger logger) {
        String[] qualifierParts = projectName.split('-')
        String firstPart = qualifierParts[0]
        String lastPart = qualifierParts[qualifierParts.size() - 1]
        String starterQualifier = "$firstPart-$lastPart"

        logger.lifecycle("lugw [logs-plugin] Название стартера: $starterQualifier")

        return starterQualifier
    }
}
