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

    String findLine(long line) {
        if (location.getLine() == 1) {
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
        } while (currentLine != line);

        return source.substring(pos);
    }

    @Override
    public String toString() {
        String result = findLine(location.getLine());
        int pos = result.indexOf('\n');
        if (pos == -1) {
            pos = result.length();
        }
        result = result.substring(0, pos) + "\n";

        result += new String(new char[(int) location.getColumn() - 1]).replace('\0', ' ') + "^";
        return result;
    }

}
