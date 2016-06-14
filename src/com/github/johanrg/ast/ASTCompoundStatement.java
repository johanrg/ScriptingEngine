package com.github.johanrg.ast;

import com.github.johanrg.compiler.Location;
import com.github.johanrg.compiler.TokenType;
import java.util.List;

/**
 * @author Johan Gustafsson
 * @since 6/14/2016.
 */

public class ASTCompoundStatement extends ASTNode {
    private final List<ASTNode> statements;

    public ASTCompoundStatement(Location location, List<ASTNode> statements) {
        super(TokenType.NONE, location);
       this.statements = statements;
    }

    public List<ASTNode> getStatements() {
        return statements;
    }
}
