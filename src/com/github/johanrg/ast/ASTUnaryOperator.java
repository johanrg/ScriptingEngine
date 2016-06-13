package com.github.johanrg.ast;

import com.github.johanrg.compiler.Location;
import com.github.johanrg.compiler.TokenType;

/**
 * @author Johan Gustafsson
 * @since 2016-06-03
 */
public class ASTUnaryOperator extends ASTNode {
    private final ASTNode node;

    public ASTUnaryOperator(TokenType tokenType, Location location, ASTNode node) {
        super(tokenType, location);
        this.node = node;
    }

    public ASTNode getNode() {
        return node;
    }

    @Override
    public String toString() {
        return super.toString() + " " + tokenType.toString();
    }
}
