package com.pluxurydolo

import com.pluxurydolo.dto.LogEntry
import com.pluxurydolo.validator.initializer.ValidationInitializer
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

        ValidationInitializer.initialize(logs, projectName, logger)
    }
}
