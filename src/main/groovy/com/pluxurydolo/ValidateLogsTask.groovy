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
            throw new GradleException('[logs-validation-plugin] Логи не найдены! Проверь параметр "loggerName" задачи :validateLogs')
        }

        List<String> prefixes = collectPrefixes(logs)

        List<String> prefixesWithInvalidLength = getPrefixesWithInvalidLength(prefixes)

        if (prefixesWithInvalidLength.size() != 0) {
            throw new GradleException("[logs-validation-plugin] У префиксов $prefixesWithInvalidLength неверная длина!")
        }

        List<String> repeatedPrefixes = getRepeatedPrefixes(prefixes)

        if (repeatedPrefixes.size() != 0) {
            throw new GradleException("[logs-validation-plugin] Префиксы $repeatedPrefixes не уникальны!")
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

        logger.lifecycle("wpfe [logs-validation-plugin] Полученные логи: $logs")
        return logs
    }

    List<String> collectPrefixes(List<String> logs) {
        List<String> prefixes = logs.stream()
                .map { it.replace('"', '') }
                .map { it.split(' ')[0] }
                .toList()

        logger.lifecycle("atbz [logs-validation-plugin] Полученные префиксы: $prefixes")
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
