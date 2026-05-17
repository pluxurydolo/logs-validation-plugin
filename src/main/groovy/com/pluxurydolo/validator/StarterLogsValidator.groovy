package com.pluxurydolo.validator

import com.pluxurydolo.dto.LogEntry

import static com.pluxurydolo.util.PrefixCollector.collectPrefixes

class StarterLogsValidator extends LogValidator {

    @Override
    List<LogEntry> retrieveInvalidLogs(List<LogEntry> logs) {
        return logs.findAll { logInvalid(it.content) }
    }

    @Override
    String errorMessageDescription(List<LogEntry> invalidLogs) {
        List<String> prefixes = collectPrefixes(invalidLogs)
        return "atus [logs-plugin] Логи стартера $prefixes имеют неверный формат (не указано название стартера в квадратных скобках)"
    }

    private static boolean logInvalid(String logContent) {
        String content = logContent.replace('"', '')
        String afterPrefix = content.substring(4).trim()

        if (!afterPrefix.startsWith('[')) {
            return true
        }

        if (!content.contains(']')) {
            return true
        }

        return false
    }
}
