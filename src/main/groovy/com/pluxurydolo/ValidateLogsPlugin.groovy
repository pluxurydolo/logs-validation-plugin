package com.pluxurydolo

import com.pluxurydolo.extension.ValidationExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileTree

import static com.pluxurydolo.util.ProjectUtils.isPlugin
import static com.pluxurydolo.util.ProjectUtils.isStarter

class ValidateLogsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        ValidationExtension validationExtension = project.extensions.create('logsValidation', ValidationExtension.class)

        project.tasks.register('validateLogs', ValidateLogsTask) {
            it.group = 'logs'

            FileTree mainFiles = project.fileTree('src/main') {
                include '**/*.java'
            }

            it.mainFiles = mainFiles
            it.loggerNames = ['LOGGER', 'log']
            it.projectName = project.name
            it.groups = validationExtension.groups
        }

        project.afterEvaluate {
            if (validationExtension.groups == null && !isStarter(project) && !isPlugin(project)) {
                project.logger.error('jyde [logs-plugin] Требуется указать зависимости для валидации логов в них: logsValidation { groups = [\'com.package\'] }')
                throw new GradleException('Не указаны зависимости для валидации логов в них')
            }
        }
    }
}
