package com.github.johanrg.ast;

import com.github.johanrg.compiler.TokenType;
import com.github.johanrg.compiler.Location;

/**
 * The super object for the AST classes.
 *
 * @author Johan Gustafsson
 * @since 2016-06-03
 */
public class ASTNode {
    final TokenType tokenType;
    final Location location;

    ASTNode(TokenType tokenType, Location location) {
        this.tokenType = tokenType;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public TokenType getType() {
        return tokenType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (location != null) {
            sb.append(String.format("(%d, %d) ", location.getLine(), location.getColumn()));
        }
        String s = getClass().getName();
        int p = s.lastIndexOf('.');
        if (p != -1) {
            sb.append(s.substring(p + 1));
        }
        sb.append(" ").append(tokenType.toString());
        return sb.toString();
    }
}
