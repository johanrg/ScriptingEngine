package com.github.johanrg.compiler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Johan Gustafsson
 * @since 2016-06-03
 */
class TokenTypeIdentifier {
    private Map<String, TokenType> tokens = new HashMap<>();

    TokenTypeIdentifier() {
        for (TokenType type : TokenType.values()) {
            if (!type.toString().equals("")) {
                tokens.put(type.toString(), type);
            }
        }
    }

    boolean isKeyword(String tokenStr) {
        TokenType token = tokens.get(tokenStr);

        return token != null && token.isKeyword();
    }

    boolean isNumber(TokenType symbol) {
        TokenTypeGroup g = symbol.getTokenTypeGroup();
        return g == TokenTypeGroup.VARIABLE_TYPE &&
                (symbol == TokenType.TYPE_INT || symbol == TokenType.TYPE_DOUBLE || symbol == TokenType.TYPE_FLOAT);
    }

    boolean isOperator(String tokenStr) {
        TokenType token = tokens.get(tokenStr);

        return token != null && token.isOperator();
    }

    TokenType getType(String tokenStr) {
        TokenType token = tokens.get(tokenStr);
        if (token != null) {
            return token;
        } else {
            return TokenType.NONE;
        }
    }
}
