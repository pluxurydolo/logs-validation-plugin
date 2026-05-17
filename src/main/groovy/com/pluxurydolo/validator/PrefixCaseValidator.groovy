package com.pluxurydolo.validator

import com.pluxurydolo.dto.LogEntry

import static com.pluxurydolo.util.PrefixCollector.collectPrefixes
import static java.util.Locale.US

class PrefixCaseValidator extends LogValidator {

    @Override
    List<LogEntry> retrieveInvalidLogs(List<LogEntry> logs) {
        return logs.findAll { prefixCaseInvalid(it) }
    }

    @Override
    String errorMessageDescription(List<LogEntry> invalidLogs) {
        List<String> prefixes = collectPrefixes(invalidLogs)
        return "taae [logs-plugin] Префиксы $prefixes содержат заглавные буквы"
    }

    private static boolean prefixCaseInvalid(LogEntry logEntry) {
        String prefix = getPrefix(logEntry)
        return prefix.toLowerCase(US) != prefix
    }
}
