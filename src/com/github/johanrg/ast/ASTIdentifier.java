package com.github.johanrg.ast;

import com.github.johanrg.compiler.Location;
import com.github.johanrg.compiler.TokenType;

/**
 * @author Johan Gustafsson
 * @since 2016-06-03
 */
public class ASTIdentifier extends ASTNode {
    private final String name;
    private final ASTNode node;

    public ASTIdentifier(TokenType tokenType, Location location, String name, ASTNode node) {
        super(tokenType, location);
        this.name = name;
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public ASTNode getNode() {
        return node;
    }

    @Override
    public String toString() {
        return super.toString() + " " + name;
    }
}
