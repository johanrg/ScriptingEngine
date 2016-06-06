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

    }

    public CompilerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
