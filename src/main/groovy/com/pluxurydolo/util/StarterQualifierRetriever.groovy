package com.pluxurydolo.util

import static java.util.Arrays.stream
import static java.util.stream.Collectors.joining

class StarterQualifierRetriever {
    static String retrieve(String projectName) {
        String[] qualifierParts = projectName.split('-')
        String starterQualifier = starterQualifier(qualifierParts)
        return starterQualifier
    }

    private static String starterQualifier(String[] qualifierParts) {
        return stream(qualifierParts)
                .filter { part -> part != 'spring' && part != 'boot' }
                .collect(joining('-'))
    }
}
