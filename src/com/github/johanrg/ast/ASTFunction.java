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
    private final ASTCompoundStatement compoundStatement;

    public ASTFunction(TokenType type, Location location, String name, List<ASTNode> parameters, ASTCompoundStatement compoundStatement) {
        super(type, location);
        this.name = name;
        this.parameters = parameters;
        this.compoundStatement = compoundStatement;
    }

    public String getName() {
        return name;
    }

    public List<ASTNode> getParameters() {
        return parameters;
    }

    public ASTCompoundStatement getCompoundStatement() {
        return compoundStatement;
    }

}
