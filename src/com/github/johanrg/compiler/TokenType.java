package com.github.johanrg.compiler;

/**
 * @author Johan Gustafsson
 * @since 2016-05-28
 */
public enum TokenType {
    NONE("", TokenTypeGroup.NONE, 0, false),

    IDENTIFIER("", TokenTypeGroup.IDENTIFIER, 0, false),

    TYPEDEF_INT("", TokenTypeGroup.TYPEDEF_VALUE, 0, false),
    TYPEDEF_FLOAT("", TokenTypeGroup.TYPEDEF_VALUE, 0, false),
    TYPEDEF_DOUBLE("", TokenTypeGroup.TYPEDEF_VALUE, 0, false),
    TYPEDEF_STRING("", TokenTypeGroup.TYPEDEF_VALUE, 0, false),

    FOR("for", TokenTypeGroup.TYPEDEF, 0, false),
    INT("int", TokenTypeGroup.TYPEDEF, 0, false),
    FLOAT("float", TokenTypeGroup.TYPEDEF, 0, false),
    DOUBLE("double", TokenTypeGroup.TYPEDEF, 0, false),
    STRING("string", TokenTypeGroup.TYPEDEF, 0, false),

    ASSIGNMENT("=", TokenTypeGroup.ASSIGNMENT_OPERATOR, 1, true),
    ADD_ASSIGNMENT("+=", TokenTypeGroup.ASSIGNMENT_OPERATOR, 1, true),
    SUB_ASSIGNMENT("-=", TokenTypeGroup.ASSIGNMENT_OPERATOR, 1, true),
    MUL_ASSIGNMENT("*=", TokenTypeGroup.ASSIGNMENT_OPERATOR, 1, true),
    DIV_ASSIGNMENT("/=", TokenTypeGroup.ASSIGNMENT_OPERATOR, 1, true),
    MOD_ASSIGNMENT("%=", TokenTypeGroup.ASSIGNMENT_OPERATOR, 1, true),

    LOGICAL_OR("||", TokenTypeGroup.BINARY_OPERATOR, 3, false),

    LOGICAL_AND("&&", TokenTypeGroup.BINARY_OPERATOR, 4, false),

    EQUAL("==", TokenTypeGroup.EQUALITY_OPERATOR, 8, false),
    NOT_EQUAL("!=", TokenTypeGroup.EQUALITY_OPERATOR, 8, false),

    BINOP_ADD("+", TokenTypeGroup.BINARY_OPERATOR, 11, false),
    BINOP_SUBTRACT("-", TokenTypeGroup.BINARY_OPERATOR, 11, false),

    BINOP_DIV("/", TokenTypeGroup.BINARY_OPERATOR, 12, false),
    BINOP_MOD("%", TokenTypeGroup.BINARY_OPERATOR, 12, false),
    BINOP_MUL("*", TokenTypeGroup.BINARY_OPERATOR, 12, false),

    UNARY_PLUS("", TokenTypeGroup.UNARY_OPERATOR, 13, true), // These must be set manually and can not be found
    UNARY_MINUS("", TokenTypeGroup.UNARY_OPERATOR, 13, true), // automatically since they conflict with regular +-
    UNARY_PRE_INCREMENT("++", TokenTypeGroup.UNARY_OPERATOR, 13, true),
    UNARY_PRE_DECREMENT("--", TokenTypeGroup.UNARY_OPERATOR, 13, true),
    UNARY_LOGICAL_NEGATION("!", TokenTypeGroup.UNARY_OPERATOR, 13, true),
    BINOP_EXPONENT("^", TokenTypeGroup.BINARY_OPERATOR, 13, true),

    UNARY_POST_INCREMENT("++", TokenTypeGroup.UNARY_OPERATOR, 14, true),
    UNARY_POST_DECREMENT("--", TokenTypeGroup.UNARY_OPERATOR, 14, true),

    OPEN_PARENTHESES("(", TokenTypeGroup.DELIMITER, 0, false), // Is seen as having precedence 15 in expressions but are handled
    CLOSE_PARENTHESES(")", TokenTypeGroup.DELIMITER, 0, false), // separately and can be seen as just having the highest precedence of all.
    OPEN_BRACE("{", TokenTypeGroup.DELIMITER, 0, false),
    CLOSE_BRACE("}", TokenTypeGroup.DELIMITER, 0, false),
    OPEN_BRACKET("[", TokenTypeGroup.DELIMITER, 0, false),
    CLOSE_BRACKET("]", TokenTypeGroup.DELIMITER, 0, false),
    END_OF_STATEMENT(";", TokenTypeGroup.DELIMITER, 0, true),
    COMMA(",", TokenTypeGroup.DELIMITER, 0, true),
    COLON(":", TokenTypeGroup.DELIMITER, 0, true);

    private final String symbol;
    private final TokenTypeGroup tokenTypeGroup;
    private final int precedence;
    private final boolean rightAssociative;

    TokenType(String symbol, TokenTypeGroup tokenTypeGroup, int precedence, boolean rightAssociative) {
        this.symbol = symbol;
        this.tokenTypeGroup = tokenTypeGroup;
        this.precedence = precedence;
        this.rightAssociative = rightAssociative;
    }

    public boolean isOperator() {
        return this.tokenTypeGroup == TokenTypeGroup.BINARY_OPERATOR ||
                this.tokenTypeGroup == TokenTypeGroup.UNARY_OPERATOR ||
                this.tokenTypeGroup == TokenTypeGroup.EQUALITY_OPERATOR ||
                this.tokenTypeGroup == TokenTypeGroup.ASSIGNMENT_OPERATOR;
    }

    public boolean isPrecedensOperator() {
        return this.tokenTypeGroup == TokenTypeGroup.DELIMITER;
    }

    public boolean isBracer() {
        return this == OPEN_BRACE || this == CLOSE_BRACE;
    }

    public boolean isBinaryOperator() {
        return tokenTypeGroup == TokenTypeGroup.BINARY_OPERATOR;
    }

    public boolean isOrCanBeUnaryOperator() {
        return tokenTypeGroup == TokenTypeGroup.UNARY_OPERATOR ||
                this == BINOP_ADD || this == BINOP_SUBTRACT;
    }

    public boolean isAssignmentOperator() {
        return tokenTypeGroup == TokenTypeGroup.ASSIGNMENT_OPERATOR;
    }

    public boolean isUnaryOperator() {
        return tokenTypeGroup == TokenTypeGroup.UNARY_OPERATOR;
    }

    public boolean isNumber() {
        return this == TYPEDEF_INT || this == TYPEDEF_FLOAT || this == TYPEDEF_DOUBLE;
    }

    public boolean isIdentifier() {
        return this == IDENTIFIER;
    }

    public boolean isKeyword() {
        return this.tokenTypeGroup == TokenTypeGroup.KEYWORD;
    }

    public boolean isTypeDef() {
        return this == TokenType.TYPEDEF_INT || this == TokenType.TYPEDEF_FLOAT ||
                this == TokenType.TYPEDEF_DOUBLE || this == TokenType.TYPEDEF_STRING;
    }

    public boolean isKeywordType() {
        return this == TokenType.TYPEDEF_INT || this == TokenType.TYPEDEF_FLOAT || this == TokenType.TYPEDEF_DOUBLE;
    }
    public boolean isRightAssociative() {
        return rightAssociative;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (symbol.length() > 0) {
            sb.append(symbol);
            sb.append(" ").append(super.toString());
            return sb.toString();
        } else {
            return super.toString();
        }
    }

    public TokenTypeGroup getTokenTypeGroup() {
        return tokenTypeGroup;
    }

    public int getPrecedence() {
        return precedence;
    }

    public String getSymbol() {
        return symbol;
    }
}