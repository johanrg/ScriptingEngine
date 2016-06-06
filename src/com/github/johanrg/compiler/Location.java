package com.github.johanrg.compiler;

/**
 * @author Johan Gustafsson
 * @since 2016-06-03
 */
public class Location {
    private final long line;
    private final long column;

    public Location(long line, long column) {
        this.line = line;
        this.column = column;
    }

    public long getLine() {
        return line;
    }

    public long getColumn() {
        return column;
    }
}
