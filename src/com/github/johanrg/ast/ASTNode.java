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
        String s = "ASTNode: " + tokenType.toString();
        if (location != null) {
            s += String.format(" (%d, %d)", location.getLine(), location.getColumn());
        }
        return s;
    }
}
