package com.github.johanrg.ast;

import com.github.johanrg.compiler.Location;
import com.github.johanrg.compiler.TokenType;

/**
 * @author Johan Gustafsson
 * @since 2016-06-05
 */
public class ASTVariable extends ASTNode {
    private final String name;
    private Object value;

    public ASTVariable(TokenType tokenType, Location location, String name, Object value) {
        super(tokenType, location);
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(" ").append(getName()).append(" = ");;
        if (tokenType == TokenType.TYPEDEF_DOUBLE) {
            sb.append(Double.toString((Double) value));
        } else if (tokenType == TokenType.TYPEDEF_FLOAT) {
            sb.append(Float.toString((Float) value));
        } else if (tokenType == TokenType.TYPEDEF_INT) {
            sb.append(Integer.toString((Integer) value));
        }
        return sb.toString();
    }
}

