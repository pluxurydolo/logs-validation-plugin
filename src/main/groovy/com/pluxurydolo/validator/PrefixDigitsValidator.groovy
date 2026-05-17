package com.pluxurydolo.validator

import com.pluxurydolo.dto.LogEntry
import com.pluxurydolo.util.PrefixCollector

import static java.lang.Character.isDigit

class PrefixDigitsValidator extends LogValidator {

    @Override
    List<LogEntry> retrieveInvalidLogs(List<LogEntry> logs) {
        return logs.findAll { containsDigits(it) }
    }

    @Override
    String errorMessageDescription(List<LogEntry> invalidLogs) {
        List<String> prefixes = PrefixCollector.collectPrefixes(invalidLogs)
        return "mkbe [logs-plugin] Префиксы $prefixes содержат цифры"
    }

    private static boolean containsDigits(LogEntry logEntry) {
        String prefix = getPrefix(logEntry)

        return prefix.chars()
                .anyMatch { isDigit(it) }
    }
}
