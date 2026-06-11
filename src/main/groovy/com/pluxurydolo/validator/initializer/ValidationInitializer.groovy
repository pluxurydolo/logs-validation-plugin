package com.pluxurydolo.validator.initializer

import com.pluxurydolo.dto.LogEntry
import com.pluxurydolo.dto.ValidationResult
import com.pluxurydolo.util.StarterQualifierRetriever
import com.pluxurydolo.validator.*
import org.gradle.api.GradleException
import org.gradle.api.logging.Logger

import static com.pluxurydolo.dto.ValidationResult.FAILURE

class ValidationInitializer {
    static void initialize(List<LogEntry> logs, String projectName, Logger logger) {
        List<LogValidator> validators = new ArrayList<>(requiredValidators())

        if (projectName.endsWith('-starter')) {
            String starterQualifier = StarterQualifierRetriever.retrieve(projectName)
            LogValidator starterLogsValidator = new StarterLogsValidator(starterQualifier)
            validators.add(starterLogsValidator)
        }

        List<ValidationResult> failedResults = validators.stream()
                .map { it.validate(logs, logger) }
                .filter { it == FAILURE }
                .toList()

        if (!failedResults.empty) {
            throw new GradleException('Валидация логов завершилась неуспешно')
        }
    }

    private static List<LogValidator> requiredValidators() {
        LogValidator prefixLengthValidator = new PrefixLengthValidator()
        LogValidator prefixCaseValidator = new PrefixCaseValidator()
        LogValidator prefixDigitsValidator = new PrefixDigitsValidator()
        LogValidator repeatedPrefixesValidator = new RepeatedPrefixesValidator()
        return List.of(prefixLengthValidator, prefixCaseValidator, prefixDigitsValidator, repeatedPrefixesValidator)
    }
}
