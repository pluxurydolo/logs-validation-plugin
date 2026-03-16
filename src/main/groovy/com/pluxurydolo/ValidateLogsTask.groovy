package com.pluxurydolo

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.tasks.*

import java.util.function.Function

import static java.util.stream.Collectors.counting
import static java.util.stream.Collectors.groupingBy
import static org.gradle.api.tasks.PathSensitivity.RELATIVE

@CacheableTask
class ValidateLogsTask extends DefaultTask {

    @InputFiles
    @PathSensitive(value = RELATIVE)
    ConfigurableFileTree files

    @Input
    String loggerName

    @TaskAction
    void validateLogs() {
        List<String> logs = collectLogs()

        if (logs.isEmpty()) {
            throw new GradleException('Logs were not found! Check parameter "loggerName" of :validateLogs')
        }

        List<String> prefixes = collectPrefixes(logs)

        List<String> prefixesWithInvalidLength = getPrefixesWithInvalidLength(prefixes)

        if (prefixesWithInvalidLength.size() != 0) {
            throw new GradleException("Prefixes $prefixesWithInvalidLength have invalid length!")
        }

        List<String> repeatedPrefixes = getRepeatedPrefixes(prefixes)

        if (repeatedPrefixes.size() != 0) {
            throw new GradleException("Prefixes $repeatedPrefixes are not unique!")
        }
    }

    List<String> collectLogs() {
        List<String> logs = files.collect { it.text }
                .stream()
                .flatMap { Arrays.stream(it.split('\n')) }
                .map { it.trim() }
                .filter { it.contains("$loggerName.") }
                .flatMap { Arrays.stream(it.split('\\(')) }
                .filter { it.startsWith('"') }
                .toList()

        logger.lifecycle("wpfe Collected logs: $logs")
        return logs
    }

    List<String> collectPrefixes(List<String> logs) {
        List<String> prefixes = logs.stream()
                .map { it.replace('"', '') }
                .map { it.split(' ')[0] }
                .toList()

        logger.lifecycle("atbz Collected prefixes: $prefixes")
        return prefixes
    }

    static List<String> getPrefixesWithInvalidLength(List<String> prefixes) {
        return prefixes.stream()
                .filter { it.length() != 4 }
                .toList()
    }

    static List<String> getRepeatedPrefixes(List<String> prefixes) {
        return prefixes.stream()
                .collect(groupingBy(Function.identity(), counting()))
                .entrySet()
                .stream()
                .filter { it.getValue() != 1 }
                .map { it.getKey() }
                .toList()
    }
}
