package com.pluxurydolo.validator

import com.pluxurydolo.dto.LogEntry

import static com.pluxurydolo.collector.PrefixCollector.collectPrefixes

class PrefixLengthValidator extends LogValidator {

    @Override
    List<LogEntry> retrieveInvalidLogs(List<LogEntry> logs) {
        return logs.findAll { prefixLengthInvalid(it) }
    }

    @Override
    String errorMessageDescription(List<LogEntry> invalidLogs) {
        List<String> prefixes = collectPrefixes(invalidLogs)
        return "hunk [logs-plugin] У префиксов $prefixes неверная длина"
    }

    private static boolean prefixLengthInvalid(LogEntry logEntry) {
        String prefix = getPrefix(logEntry)
        return prefix.length() != 4
    }
}
