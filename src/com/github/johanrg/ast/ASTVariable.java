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

    @Override
    public String toString() {
        String s = "ASTIdentifier: " + name;
        if (location != null) {
            s += String.format(" (%d, %d)", location.getLine(), location.getColumn());
        }
        return s;
    }}
