package com.github.johanrg.ast;

import com.github.johanrg.compiler.Location;
import com.github.johanrg.compiler.TokenType;

/**
 * @author Johan Gustafsson
 * @since 6/14/2016.
 */

public class ASTFunction extends ASTNode {
    private final String name;
    private final ASTNode statement;
    private final TokenType returnType;

    public ASTFunction(TokenType tokenType, Location location, String name, ASTNode statement, TokenType returnType) {
        super(tokenType, location);
        this.name = name;
        this.statement = statement;
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public ASTNode getStatement() {
        return statement;
    }
}
