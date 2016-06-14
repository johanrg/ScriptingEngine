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

    boolean isTypeDefiner(String tokenStr) {
        TokenType type = tokens.get(tokenStr);
        return type != null && type.getTokenTypeGroup() == TokenTypeGroup.TYPEDEF;
    }

    boolean isKeyword(String tokenStr) {
        TokenType type = tokens.get(tokenStr);

        return type != null && type.isKeyword();
    }

    boolean isNumber(TokenType tokenType) {
        TokenTypeGroup g = tokenType.getTokenTypeGroup();
        return tokenType == TokenType.TYPEDEF_INT || tokenType == TokenType.TYPEDEF_DOUBLE || tokenType == TokenType.TYPEDEF_FLOAT;
    }

    boolean isOperator(String tokenStr) {
        TokenType type = tokens.get(tokenStr);

        return type != null && type.isOperator();
    }

    TokenType getType(String tokenStr) {
        TokenType type = tokens.get(tokenStr);
        if (type != null) {
            return type;
        } else {
            return TokenType.NONE;
        }
    }
}
