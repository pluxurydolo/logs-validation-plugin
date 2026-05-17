package com.pluxurydolo.validator

import com.pluxurydolo.dto.LogEntry
import org.gradle.api.logging.Logger

import static com.pluxurydolo.util.PrefixCollector.collectPrefixes

class StarterLogsValidator extends LogValidator {
    private final String projectName
    private final Logger logger

    StarterLogsValidator(String projectName, Logger logger) {
        this.projectName = projectName
        this.logger = logger
    }

    @Override
    List<LogEntry> retrieveInvalidLogs(List<LogEntry> logs) {
        return logs.findAll { logInvalid(it.content) }
    }

    @Override
    String errorMessageDescription(List<LogEntry> invalidLogs) {
        List<String> prefixes = collectPrefixes(invalidLogs)
        return "atus [logs-plugin] Логи стартера $prefixes имеют неверный формат (не указано название стартера в квадратных скобках)"
    }

    private boolean logInvalid(String logContent) {
        String content = logContent.replace('"', '')
        String afterPrefix = content.substring(4).trim()
        String starterQualifer = starterQualifier()

        if (!afterPrefix.startsWith("[$starterQualifer]")) {
            return true
        }

        return false
    }

    private String starterQualifier() {
        String[] qualifierParts = projectName.split('-')
        String firstPart = qualifierParts[0]
        String lastPart = qualifierParts[qualifierParts.size() - 1]
        String starterQualifier = "$firstPart-$lastPart"

        logger.lifecycle("lugw [logs-plugin] Название стартера: $starterQualifier")

        return starterQualifier
    }
}
