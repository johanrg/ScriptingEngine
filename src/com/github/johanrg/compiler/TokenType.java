package com.github.johanrg.compiler;

/**
 * @author Johan Gustafsson
 * @since 2016-05-28
 */
public enum TokenType {
    NONE("", TokenTypeGroup.NONE, 0, false),

    FOR("for", TokenTypeGroup.KEYWORD, 0, false),
    IDENTIFIER("", TokenTypeGroup.IDENTIFIER, 0, false),

    TYPE_DOUBLE("double", TokenTypeGroup.VARIABLE_TYPE, 0, false),
    TYPE_FLOAT("float", TokenTypeGroup.VARIABLE_TYPE, 0, false),
    TYPE_INT("int", TokenTypeGroup.VARIABLE_TYPE, 0, false),
    TYPE_STRING("string", TokenTypeGroup.VARIABLE_TYPE, 0, false),

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

    OPEN_PARENTHESES("(", TokenTypeGroup.DELIMITER, 0, false), // Should have precedence 15 but are handled
    CLOSE_PARENTHESES(")", TokenTypeGroup.DELIMITER, 0, false); // separately, setting them will mess up the parser.

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

    public boolean isBinaryOperator() {
        return tokenTypeGroup == TokenTypeGroup.BINARY_OPERATOR;
    }

    public boolean isOrCanBeUnaryOperator() {
        return tokenTypeGroup == TokenTypeGroup.UNARY_OPERATOR ||
                this == BINOP_ADD || this == BINOP_SUBTRACT;
    }

    public boolean isUnaryOperator() {
        return tokenTypeGroup == TokenTypeGroup.UNARY_OPERATOR;
    }

    public boolean isNumber() {
        return this == TYPE_INT || this == TYPE_FLOAT || this == TYPE_DOUBLE;
    }

    public boolean isKeyword() {
        return this.tokenTypeGroup == TokenTypeGroup.KEYWORD;
    }

    public boolean isRightAssociative() {
        return rightAssociative;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public TokenTypeGroup getTokenTypeGroup() {
        return tokenTypeGroup;
    }

    public int getPrecedence() {
        return precedence;
    }

    }