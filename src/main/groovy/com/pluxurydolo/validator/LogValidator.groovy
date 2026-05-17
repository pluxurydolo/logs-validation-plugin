package com.pluxurydolo.validator

import com.pluxurydolo.dto.LogEntry
import org.gradle.api.GradleException

abstract class LogValidator {
    void validate(List<LogEntry> logs) {
        List<LogEntry> invalidLogs = retrieveInvalidLogs(logs)

        if (invalidLogs.size() != 0) {
            String errorMessageDescription = errorMessageDescription(invalidLogs)

            StringBuilder errorMessage = new StringBuilder(errorMessageDescription)
                    .append('\n')

            invalidLogs.each { errorMessage.append("  ${it.location}\n") }
            throw new GradleException(errorMessage.toString())
        }
    }

    static String getPrefix(LogEntry logEntry) {
        return logEntry.content
                .replace('"', '')
                .split(' ')[0]
    }

    abstract List<LogEntry> retrieveInvalidLogs(List<LogEntry> logs)

    abstract String errorMessageDescription(List<LogEntry> invalidLogs)
}
