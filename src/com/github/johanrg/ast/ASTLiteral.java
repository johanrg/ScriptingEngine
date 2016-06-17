package com.github.johanrg.ast;

import com.github.johanrg.compiler.Location;
import com.github.johanrg.compiler.TokenType;

/**
 * @author Johan Gustafsson
 * @since 2016-06-03
 */
public class ASTLiteral extends ASTNode {
    private final Object value;

    public ASTLiteral(TokenType tokenType, Location location, Object value) {
        super(tokenType, location);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public boolean isZero() {
        if (tokenType == TokenType.TYPEDEF_INT) {
            return ((Integer) value).equals(0);
        } else if (tokenType == TokenType.TYPEDEF_FLOAT) {
            return ((Float) value).equals(0.f);
        } else if (tokenType == TokenType.TYPEDEF_DOUBLE) {
            return ((Double) value).equals(0.0);
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(" ");
        if (tokenType == TokenType.TYPEDEF_DOUBLE) {
            sb.append(Double.toString((Double) value));
        } else if (tokenType == TokenType.TYPEDEF_FLOAT) {
            sb.append(Float.toString((Float) value));
        } else if (tokenType == TokenType.TYPEDEF_INT) {
            sb.append(Integer.toString((Integer) value));
        } else if (tokenType == TokenType.TYPEDEF_STRING)
            sb.append("\"").append(value).append("\"");
        return sb.toString();
    }
}
