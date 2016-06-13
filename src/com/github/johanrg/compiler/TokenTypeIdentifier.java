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

    boolean isNumber(TokenType tokenType) {
        TokenTypeGroup g = tokenType.getTokenTypeGroup();
        return tokenType == TokenType.TYPEDEF_INT || tokenType == TokenType.TYPEDEF_DOUBLE || tokenType == TokenType.TYPEDEF_FLOAT;
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
