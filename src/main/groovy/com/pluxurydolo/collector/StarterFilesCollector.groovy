package com.pluxurydolo.collector

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ModuleVersionIdentifier
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.file.Directory
import org.gradle.api.file.FileTree
import org.gradle.api.provider.Provider

class StarterFilesCollector {
    static FileTree collect(Project project, List<String> groups) {
        Configuration sourcesConfig = project.configurations.create('logValidatorSources')
        sourcesConfig.transitive = false

        Configuration resolvableConfig = project.configurations.named('runtimeClasspath').getOrNull()

        if (resolvableConfig == null) {
            resolvableConfig = project.configurations.named('compileClasspath').getOrNull()
        }

        resolvableConfig.resolvedConfiguration.resolvedArtifacts.each { ResolvedArtifact artifact ->
            ModuleVersionIdentifier id = artifact.moduleVersion.id

            if (groups.contains(id.group)) {
                project.logger.lifecycle("jvch [logs-plugin] Валидация логов: ${id.group}:${id.name}:${id.version}")

                Dependency sourceDep = project.dependencies.create("${id.group}:${id.name}:${id.version}:sources")
                sourcesConfig.dependencies.add(sourceDep)
            }
        }

        Provider<Directory> unpackDir = project.layout.buildDirectory.dir('tmp/starterSources')
        Set<File> resolved = sourcesConfig.resolve()

        resolved.each { jar ->
            project.copy {
                from project.zipTree(jar)
                into unpackDir
            }
        }

        return project.fileTree(unpackDir.get().asFile) {
            include '**/*.java'
        }
    }
}
