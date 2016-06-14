package com.github.johanrg.ast;

import com.github.johanrg.compiler.Location;
import com.github.johanrg.compiler.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Johan Gustafsson
 * @since 6/14/2016.
 */

public class ASTFunction extends ASTNode {
    private final String name;
    private final List<ASTNode> parameters;
    private final ASTCompoundStatement statement;
    private final TokenType returnType;

    public ASTFunction(Location location, String name, List<ASTNode> parameters, ASTCompoundStatement statement, TokenType returnType) {
        super(TokenType.NONE, location);
        this.name = name;
        this.parameters = parameters;
        this.statement = statement;
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public List<ASTNode> getParameters() {
        return parameters;
    }

    public ASTNode getStatement() {
        return statement;
    }

    public TokenType getReturnType() {
        return returnType;
    }
}
