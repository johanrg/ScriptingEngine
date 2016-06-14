package com.github.johanrg.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Johan Gustafsson
 * @since 6/14/2016.
 */

public class ASTCompoundStatement {
    private final List<ASTNode> statements;

    public ASTCompoundStatement(List<ASTNode> statements) {
       this.statements = statements;
    }

    public List<ASTNode> getStatements() {
        return statements;
    }
}
