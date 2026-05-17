package com.pluxurydolo.validator

import com.pluxurydolo.dto.LogEntry

import java.util.function.Function

import static com.pluxurydolo.util.PrefixCollector.collectPrefixes
import static java.util.Collections.emptyList
import static java.util.stream.Collectors.counting
import static java.util.stream.Collectors.groupingBy

class RepeatedPrefixesValidator extends LogValidator {

    @Override
    List<LogEntry> retrieveInvalidLogs(List<LogEntry> logs) {
        List<String> prefixes = collectPrefixes(logs)
        List<String> repeatedPrefixes = repeatedPrefixes(prefixes)

        if (repeatedPrefixes.isEmpty()) {
            return emptyList()
        }

        return logs.findAll { prefixRepeated(it, repeatedPrefixes) }
    }

    @Override
    String errorMessageDescription(List<LogEntry> invalidLogs) {
        List<String> prefixes = collectPrefixes(invalidLogs)
        return "gyfc [logs-plugin] Префиксы $prefixes не уникальны"
    }

    private static boolean prefixRepeated(LogEntry logEntry, List<String> repeatedPrefixes) {
        String prefix = getPrefix(logEntry)
        return repeatedPrefixes.contains(prefix)
    }

    private static List<String> repeatedPrefixes(List<String> prefixes) {
        return prefixes.stream()
                .collect(groupingBy(Function.identity(), counting()))
                .entrySet()
                .stream()
                .filter { it.getValue() != 1 }
                .map { it.getKey() }
                .toList()
    }
}
