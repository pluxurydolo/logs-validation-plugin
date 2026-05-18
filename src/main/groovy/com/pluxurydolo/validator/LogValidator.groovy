package com.pluxurydolo.validator

import com.pluxurydolo.dto.LogEntry
import com.pluxurydolo.dto.ValidationResult
import org.gradle.api.logging.Logger

import static com.pluxurydolo.dto.ValidationResult.FAILURE
import static com.pluxurydolo.dto.ValidationResult.SUCCESS
import static com.pluxurydolo.util.PrefixGenerator.generate

abstract class LogValidator {
    ValidationResult validate(List<LogEntry> logs, Logger logger) {
        List<LogEntry> invalidLogs = retrieveInvalidLogs(logs)

        if (invalidLogs.size() != 0) {
            String errorMessageDescription = errorMessageDescription(invalidLogs)
            String advisedPrefix = generate()

            StringBuilder errorMessage = new StringBuilder(errorMessageDescription)
                    .append('\n')
                    .append("Предложенный префикс: $advisedPrefix\n")

            invalidLogs.each { errorMessage.append("  ${it.location}\n") }
            logger.error(errorMessage.toString())

            return FAILURE
        }

        return SUCCESS
    }

    static String getPrefix(LogEntry logEntry) {
        return logEntry.content
                .replace('"', '')
                .split(' ')[0]
    }

    abstract List<LogEntry> retrieveInvalidLogs(List<LogEntry> logs)

    abstract String errorMessageDescription(List<LogEntry> invalidLogs)
}
