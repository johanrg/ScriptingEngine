package com.github.johanrg.ast;

import com.github.johanrg.compiler.Location;
import com.github.johanrg.compiler.TokenType;

/**
 * @author Johan Gustafsson
 * @since 2016-06-03
 */
public class ASTBinaryOperator extends ASTNode {
    private final ASTNode leftNode;
    private final ASTNode rightNode;

    public ASTBinaryOperator(TokenType tokenType, Location location, ASTNode leftNode, ASTNode rightNode) {
        super(tokenType, location);
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public ASTNode getLeftNode() {
        return leftNode;
    }

    public ASTNode getRightNode() {
        return rightNode;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
