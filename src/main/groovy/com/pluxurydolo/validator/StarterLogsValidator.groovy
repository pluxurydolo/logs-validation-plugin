package com.pluxurydolo.validator

import com.pluxurydolo.dto.LogEntry

import static com.pluxurydolo.util.PrefixCollector.collectPrefixes

class StarterLogsValidator extends LogValidator {
    private final String starterQualifier

    StarterLogsValidator(String starterQualifier) {
        this.starterQualifier = starterQualifier
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

        if (!content.contains("[$starterQualifier]")) {
            return true
        }

        return false
    }
}
