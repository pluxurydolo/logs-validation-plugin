package com.pluxurydolo.dto

class IndexedLine {
    private final String line
    private final int index

    IndexedLine(String line, int index) {
        this.line = line
        this.index = index
    }

    String getLine() {
        return line
    }

    int getIndex() {
        return index
    }
}
