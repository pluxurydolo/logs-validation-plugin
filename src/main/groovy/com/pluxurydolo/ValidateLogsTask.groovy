package com.pluxurydolo

import com.pluxurydolo.collector.FilesCollector
import com.pluxurydolo.collector.LogsCollector
import com.pluxurydolo.dto.LogEntry
import com.pluxurydolo.validator.initializer.ValidationInitializer
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.*

import static org.gradle.api.tasks.PathSensitivity.RELATIVE

@CacheableTask
class ValidateLogsTask extends DefaultTask {

    @InputFiles
    @PathSensitive(value = RELATIVE)
    FileTree mainFiles

    @Input
    List<String> loggerNames

    @Input
    String projectName

    @Input
    @Optional
    List<String> groups = null

    @TaskAction
    void validateLogs() {
        FileTree files = FilesCollector.collect(project, mainFiles, groups)

        List<LogEntry> logs = LogsCollector.collect(files, loggerNames, logger)

        ValidationInitializer.initialize(logs, projectName, logger)
    }
}
