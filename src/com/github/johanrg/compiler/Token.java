package com.github.johanrg.compiler;

/**
 * @author Johan Gustafsson
 * @since 2016-06-03
 */
class Token {
    private TokenType symbol;
    private final Location location;
    private final Object value;

    Token(TokenType symbol, Location location, Object value) {
        this.symbol = symbol;
        this.location = location;
        this.value = value;
    }

    @Override
    public String toString() {
        if (value instanceof Integer) {
            return String.format("%s %s (%d, %d)", symbol, Integer.toString((Integer) value), location.getLine(), location.getColumn());
        } else if (value instanceof Float) {
            return String.format("%s %s (%d, %d)", symbol, Float.toString((Float) value), location.getLine(), location.getColumn());
        } else if (value instanceof Double) {
            return String.format("%s %s (%d, %d)", symbol, Double.toString((Double) value), location.getLine(), location.getColumn());
        } else if (value instanceof String) {
            return String.format("%s %s (%d, %d)", symbol, (String) value, location.getLine(), location.getColumn());
        }

        return String.format("%s (%d, %d)", symbol, location.getLine(), location.getColumn());
    }

    Location getLocation() {
        return location;
    }

    TokenType getSymbolType() {
        return symbol;
    }

    Object getValue() {
        return value;
    }
}
