package com.github.johanrg.compiler;

/**
 * This class will be used to present the location of a compiler error in the source.
 *
 * @author Johan Gustafsson
 * @since 2016-06-05
 */
public class SourceLocation {
    private final String source;
    private final Location location;

    SourceLocation(String source, Location location) {
        this.source = source;
        this.location = location;
    }

    String findLine(int line) {
        if (line == 1) {
            return source;
        }

        int currentLine = 1;
        int pos = 0;

        do {
            pos = source.indexOf('\n', pos + 1);
            if (pos == -1) {
                return null;
            }
            ++currentLine;
        } while (currentLine < line);

        return source.substring(pos);
    }

    String show() {
        String result = null;
        return result;
    }
}
