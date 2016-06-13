package com.github.johanrg.compiler;

/**
 * @author Johan Gustafsson
 * @since 2016-05-28
 */
public class CompilerException extends Exception {
    CompilerException(String message) {
        super(message);
    }
    CompilerException(String message, Location location) {
        super(String.format(message + " (line %d, column %d)", location.getLine(), location.getColumn()));
    }

    CompilerException(String message, Location location, String source) {
        super(String.format("%s (line %d, column %d)\n%s", message, location.getLine(), location.getColumn(), new SourceLocation(source, location).toString()));

    }

    public CompilerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
