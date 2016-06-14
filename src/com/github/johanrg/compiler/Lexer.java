package com.github.johanrg.compiler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 *  Lexer
 *  Creates tokens from the source string
 *
 * @author Johan Gustafsson
 * @since 2016-05-28
 */
public class Lexer {
    private final String source;
    private int pos = 0;
    private int line = 1;
    private int column = 1;
    private final Queue<Token> tokens = new ArrayDeque<>();
    private final TokenTypeIdentifier identify = new TokenTypeIdentifier();
    private int balancedParantheses = 0;
    private Token previousToken = null;

    /**
     * The constructor creates tokens from the source string
     *
     * @param source string representing the script source
     * @throws CompilerException
     */
    public Lexer(String source) throws CompilerException {
        // The added 0 to the end of the string makes us not having to test the if the string has reached it's end
        // everywhere. A valid character will always have at least a 0 that follows after it and this can be checked in
        // one place and only when no other valid characters are left.
        if (source.length() == 0) {
            throw new CompilerException("Unexpected end of line", new Location(line, column));
        }
        this.source = source + "\0";

        for (; ; ) {
            char c = peekAtChar();
            if (isWhitespace(c)) {
                eatAllWhitespaces(c);

            } else if (isIdentifier(c)) {
                addToken(lexIdentifier(c));

            } else if (isOperator(c)) {
                addToken(lexOperator(c));

            } else if (isNumber(c)) {
                addToken(lexNumber(c));

            } else if (isDelimiter(c)) {
                addToken(new Token(identify.getType(c), new Location(line, column), c));
                eatTheChar();

            } else if (isEndOfStatement(c)) {
                if (balancedParantheses > 0) {
                    throw new CompilerException("Expected ')'", new Location(line, column), source);
                } else if (balancedParantheses < 0) {
                    throw new CompilerException("Expected ';'", new Location(line, column), source);
                }
                addToken(new Token(TokenType.END_OF_STATEMENT, new Location(line, column), c));
                eatTheChar();

            } else if (c == '\0') {
                break;

            } else {
                throw new CompilerException(String.format("Syntax error: '%c'", c), new Location(line, column), source);
            }
        }
    }

    /**
     * Returns a valid operator or throws a CompilerException.
     *
     * @param c Valid char from the current string.
     * @return Token
     * @throws CompilerException
     */
    private Token lexOperator(char c) throws CompilerException {
        Location location = new Location(line, column);
        StringBuilder operator = new StringBuilder();

        do {
            eatTheChar();
            operator.append(c);
            if (c == '(') {
                ++balancedParantheses;
                break;
            } else if (c == ')') { // Never more than one at a time.
                --balancedParantheses;
                break;
            }
            c = peekAtChar();
        } while (isContinuingOperator(c));

        TokenType type = identify.getType(operator.toString());
        if (type.isBinaryOperator()) {
            if (previousToken != null && (previousToken.getTokenType().isBinaryOperator() ||
                    previousToken.getTokenType() == TokenType.OPEN_PARENTHESES)) {
                if (type.isOrCanBeUnaryOperator()) {
                    if (type == TokenType.BINOP_ADD) {
                        type = TokenType.UNARY_PLUS;
                    } else if (type == TokenType.BINOP_SUBTRACT) {
                        type = TokenType.UNARY_MINUS;
                    } else {
                        throw new CompilerException("Expected value", location, source);
                    }
                }
            } else if (previousToken != null && previousToken.getTokenType().isUnaryOperator()) {
                throw new CompilerException("Expected value", location, source);
            } else if (previousToken == null && type.isBinaryOperator())
                if (type.isOrCanBeUnaryOperator()) {
                    if (type == TokenType.BINOP_ADD) {
                        type = TokenType.UNARY_PLUS;
                    } else if (type == TokenType.BINOP_SUBTRACT) {
                        type = TokenType.UNARY_MINUS;
                    } else {
                        throw new CompilerException("Expected value", location, source);
                    }
                }
            return new Token(type, location, operator.toString());
        } else if (type.isUnaryOperator()) {
            if (previousToken != null && (previousToken.getTokenType().isUnaryOperator() ||
                    previousToken.getTokenType().isNumber())) {
                throw new CompilerException("Expected value", location, source);
            }
        } else if (type.isAssignmentOperator()) {
            if (previousToken != null && !previousToken.getTokenType().isIdentifier()) {
               throw new CompilerException("Expected identifier", previousToken.getLocation(), source);
            }
        } else if (!type.isPrecedensOperator()) {
            throw new CompilerException(String.format("'%s' is not a valid operator", operator.toString()), location, source);
        }

        return new Token(type, location, operator.toString());
    }

    /**
     * Returns a token containing a float, double or integer.
     *
     * @param c Valid char from the current string.
     * @return Token
     */
    private Token lexNumber(char c) throws CompilerException {
        Location location = new Location(line, column);
        StringBuilder number = new StringBuilder();
        boolean isDouble = false;
        boolean isFloat = false;

        do {
            eatTheChar();
            number.append(c);
            c = peekAtChar();
            if (c == '.') {
                isDouble = true;
            }
            if (c == 'f') {
                isFloat = true;
            }
        } while (isContinuingNumber(c));

        if (isFloat) {
            return new Token(TokenType.TYPEDEF_FLOAT, location, Float.parseFloat(number.toString()));
        } else if (isDouble) {
            return new Token(TokenType.TYPEDEF_DOUBLE, location, Double.parseDouble(number.toString()));
        } else {
            return new Token(TokenType.TYPEDEF_INT, location, Integer.parseInt(number.toString()));
        }
    }

    /**
     * Returns a token with an identifier or reserved word.
     *
     * @param c a valid alpha char
     * @return Token
     */
    private Token lexIdentifier(char c) {
        Location location = new Location(line, column);
        StringBuilder identifier = new StringBuilder();

        do {
            eatTheChar();
            identifier.append(c);
            c = peekAtChar();
        } while (isContinuingIdentifier(c));

        if (identify.isKeyword(identifier.toString()) || identify.isTypeDefiner(identifier.toString())) {
            return new Token(identify.getType(identifier.toString()), location, identifier.toString());
        }

        return new Token(TokenType.IDENTIFIER, location, identifier.toString());
    }

    private char peekAtChar() {
        return source.charAt(pos);
    }

    private void eatTheChar() {
        ++pos;
        ++column;
    }

    /**
     * Moves the position forward while character is a white space.
     *
     * @param c Valid char from the current string.
     */
    private void eatAllWhitespaces(char c) {
        do {
            if (isNewLine(c)) {
                column = 0;
                ++line;
            }
            eatTheChar();
            c = peekAtChar();
        } while (isWhitespace(c));
    }

    /**
     * Small helper function to also set previousToken var.
     *
     * @param token new token to add.
     */
    private void addToken(Token token) {
        tokens.offer(token);
        previousToken = token;
    }

    private boolean isNumber(char c) {
        return (c >= '0' && c <= '9');
    }

    private boolean isContinuingNumber(char c) {
        return (c >= '0' && c <= '9') || c == '.' || c == 'f';
    }

    private boolean isIdentifier(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isOperator(char c) {
        return "+-/*%^=!()^|=".indexOf(c) != -1;
    }

    private boolean isContinuingOperator(char c) {
        return "+-/*%=^|".indexOf(c) != -1;
    }

    private boolean isDelimiter(char c) {
        return "()[]{}".indexOf(c) != -1;
    }

    private boolean isContinuingIdentifier(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || isNumber(c) || c == '_';
    }

    private boolean isNewLine(char c) {
        return (c == '\n');
    }

    private boolean isWhitespace(char c) {
        return (c == ' ' || c == '\t' || c == '\r' || isNewLine(c));
    }

    private boolean isEndOfStatement(char c) {
        return c == ';';
    }

    public Queue<Token> getTokens() {
        return tokens;
    }

    public String getSource() {
        return source;
    }
}
