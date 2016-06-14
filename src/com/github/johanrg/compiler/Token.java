package com.github.johanrg.compiler;

/**
 * @author Johan Gustafsson
 * @since 2016-06-03
 */
class Token {
    private TokenType tokenType;
    private final Location location;
    private final Object value;

    Token(TokenType tokenType, Location location, Object value) {
        this.tokenType = tokenType;
        this.location = location;
        this.value = value;
    }

    @Override
    public String toString() {
        if (value instanceof Integer) {
            return String.format("%s %s (%d, %d)", tokenType, Integer.toString((Integer) value), location.getLine(), location.getColumn());
        } else if (value instanceof Float) {
            return String.format("%s %s (%d, %d)", tokenType, Float.toString((Float) value), location.getLine(), location.getColumn());
        } else if (value instanceof Double) {
            return String.format("%s %s (%d, %d)", tokenType, Double.toString((Double) value), location.getLine(), location.getColumn());
        } else if (value instanceof String && tokenType == TokenType.IDENTIFIER) {
            return String.format("%s %s (%d, %d)", tokenType, (String) value, location.getLine(), location.getColumn());
        }

        return String.format("%s (%d, %d)", tokenType, location.getLine(), location.getColumn());
    }

    Location getLocation() {
        return location;
    }

    TokenType getTokenType() {
        return tokenType;
    }

    Object getValue() {
        return value;
    }
}
