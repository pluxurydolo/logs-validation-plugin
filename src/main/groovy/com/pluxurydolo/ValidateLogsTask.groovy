package com.pluxurydolo

import com.pluxurydolo.dto.LogEntry
import com.pluxurydolo.validator.*
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.tasks.*

import static com.pluxurydolo.util.LogCollector.collectLogs
import static org.gradle.api.tasks.PathSensitivity.RELATIVE

@CacheableTask
class ValidateLogsTask extends DefaultTask {

    @InputFiles
    @PathSensitive(value = RELATIVE)
    ConfigurableFileTree files

    @Input
    List<String> loggerNames

    @Input
    String projectName

    @TaskAction
    void validateLogs() {
        List<LogEntry> logs = collectLogs(files, loggerNames, logger)

        validators(projectName)
                .forEach { it.validate(logs) }
    }

    private static List<LogValidator> validators(String projectName) {
        List<LogValidator> validators = new ArrayList<>(requiredValidators())

        if (projectName.endsWith('-starter')) {
            LogValidator starterLogsValidator = new StarterLogsValidator()
            validators.add(starterLogsValidator)
        }

        return validators
    }

    private static List<LogValidator> requiredValidators() {
        LogValidator prefixLengthValidator = new PrefixLengthValidator()
        LogValidator prefixCaseValidator = new PrefixCaseValidator()
        LogValidator prefixDigitsValidator = new PrefixDigitsValidator()
        LogValidator repeatedPrefixesValidator = new RepeatedPrefixesValidator()
        return List.of(prefixLengthValidator, prefixCaseValidator, prefixDigitsValidator, repeatedPrefixesValidator)
    }
}
