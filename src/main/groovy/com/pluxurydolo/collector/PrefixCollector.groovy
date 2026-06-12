package com.pluxurydolo.collector

import com.pluxurydolo.dto.LogEntry

class PrefixCollector {
    static List<String> collectPrefixes(List<LogEntry> logs) {
        return logs.stream()
                .map { it.content }
                .map { it.replace('"', '') }
                .map { it.split(' ')[0] }
                .toList()
    }
}
