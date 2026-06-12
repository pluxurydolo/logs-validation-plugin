package com.pluxurydolo.collector

import org.gradle.api.Project
import org.gradle.api.file.FileTree

import static com.pluxurydolo.collector.StarterFilesCollector.collect

class FilesCollector {
    static FileTree collect(Project project, FileTree mainFiles, List<String> groups) {
        if (groups == null) {
            return mainFiles
        }

        FileTree starterFiles = collect(project, groups)

        return mainFiles + starterFiles
    }
}
