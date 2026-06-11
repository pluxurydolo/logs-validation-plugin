package com.pluxurydolo.util

import com.pluxurydolo.dto.IndexedLine
import com.pluxurydolo.dto.LogEntry
import org.gradle.api.file.FileTree
import org.gradle.api.logging.Logger

import java.util.stream.IntStream

class LogCollector {
    static List<LogEntry> collectLogs(FileTree files, List<String> loggerNames, Logger logger) {
        List<LogEntry> logs = files.collectMany { collectFromFile(it, loggerNames) }

        List<String> logsContent = logs.stream()
                .map { it.content }
                .toList()

        logger.lifecycle("wpfe [logs-plugin] Полученные логи: $logsContent")
        return logs
    }

    private static List<LogEntry> collectFromFile(File file, List<String> loggerNames) {
        String normalizedPath = file.absolutePath.replace('\\', '/')

        return extractFileLines(file)
                .stream()
                .filter { isLogLine(loggerNames, it.line) }
                .flatMap { extractLogEntries(it.line, file, it.index, normalizedPath).stream() }
                .toList()
    }

    private static List<IndexedLine> extractFileLines(File file) {
        String[] lines = file.text.split('\n')

        return IntStream.range(0, lines.size())
                .mapToObj { new IndexedLine(lines[it], it) }
                .toList()
    }

    private static boolean isLogLine(List<String> loggerNames, String line) {
        return loggerNames.any { line.contains("$it.") }
    }

    private static List<LogEntry> extractLogEntries(String line, File file, int lineIndex, String normalizedPath) {
        int lineNumber = lineIndex + 1
        String location = "${normalizedPath}: ${lineNumber}"

        return line.split('\\(')
                .findAll { it.startsWith('"') }
                .stream()
                .map { it.split('"')[1] }
                .map { new LogEntry(it, file, lineNumber, location) }
                .toList()
    }
}
