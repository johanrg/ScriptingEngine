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
        if (tokenType == TokenType.TYPE_INT) {
            return ((Integer) value).equals(0);
        } else if (tokenType == TokenType.TYPE_FLOAT) {
            return ((Float) value).equals(0.f);
        } else if (tokenType == TokenType.TYPE_DOUBLE) {
            return ((Double) value).equals(0.0);
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ASTLiteral: ");
        if (tokenType == TokenType.TYPE_DOUBLE) {
            sb.append(Double.toString((Double) value));
        } else if (tokenType == TokenType.TYPE_FLOAT) {
            sb.append(Float.toString((Float) value));
        } else if (tokenType == TokenType.TYPE_INT) {
            sb.append(Integer.toString((Integer) value));
        }
        if (location != null) {
            sb.append(String.format(" (%d, %d)", location.getLine(), location.getColumn()));
        }

        return sb.toString();
    }
}
