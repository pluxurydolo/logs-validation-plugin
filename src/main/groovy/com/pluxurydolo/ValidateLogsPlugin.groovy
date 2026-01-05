package com.pluxurydolo

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree

class ValidateLogsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.tasks.register('validateLogs', ValidateLogsTask) {
            group = 'logs'

            ConfigurableFileTree tree = project.fileTree(dir: 'src/main')
            tree.include '**/*.java'

            it.files = tree
            it.loggerName = 'LOGGER'
        }
    }
}
