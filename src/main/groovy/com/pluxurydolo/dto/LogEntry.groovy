package com.pluxurydolo.dto

class LogEntry {
    private final String content
    private final File file
    private final int lineNumber
    private final String location

    LogEntry(String content, File file, int lineNumber, String location) {
        this.content = content
        this.file = file
        this.lineNumber = lineNumber
        this.location = location
    }

    String getContent() {
        return content
    }

    String getLocation() {
        return location
    }
}
