package com.github.johanrg.ast;

import com.github.johanrg.compiler.Location;
import com.github.johanrg.compiler.TokenType;

/**
 * @author Johan Gustafsson
 * @since 2016-06-05
 */
public class ASTVariable extends ASTNode {
    private final String name;
    private final Object value;

    public ASTVariable(TokenType variableType, Location location, String name, Object value) {
        super(variableType, location);
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(" ").append(getName()).append(":");;
        if (tokenType == TokenType.TYPEDEF_DOUBLE) {
            sb.append(Double.toString((Double) value));
        } else if (tokenType == TokenType.TYPEDEF_FLOAT) {
            sb.append(Float.toString((Float) value));
        } else if (tokenType == TokenType.TYPEDEF_INT) {
            sb.append(Integer.toString((Integer) value));
        } else if (tokenType == TokenType.TYPEDEF_STRING) {
            sb.append("'").append((String) value).append("'");
        }
        return sb.toString();
    }
}

