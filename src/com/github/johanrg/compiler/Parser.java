package com.github.johanrg.compiler;

import com.github.johanrg.ast.*;

import java.lang.reflect.Parameter;
import java.util.*;

/**
 * The parser builds the complete AST tree for the scripting language.
 *
 * @author Johan Gustafsson
 * @since 2016-06-04
 */
public class Parser {
    private final String source;
    private final Queue<Token> tokens;
    private final ASTNode astRootNode;
    private final Stack<Token> operatorStack = new Stack<>();
    private final Stack<ASTNode> expressionStack = new Stack<>();
    private final Map<String, ASTNode> identifierList = new HashMap<>();

    public Parser(Lexer lexer) throws CompilerException {
        this.tokens = lexer.getTokens();
        this.source = lexer.getSource();

        while (!tokens.isEmpty()) {
            ASTNode statement = parseStatement();
            System.out.println(statement);
        }
        astRootNode = null;
        //astRootNode = parseExpression();
    }


    /**
     * Parses all types of statements and expressions.
     *
     * @return ASTNode contains the statement
     * @throws CompilerException
     */
    private ASTNode parseStatement() throws CompilerException {
        while (!tokens.isEmpty()) {
            if (tokens.peek().getTokenType() == TokenType.OPEN_BRACE) {
                return parseCompoundStatement();

            } else if (tokens.peek().getTokenType().getTokenTypeGroup() == TokenTypeGroup.TYPEDEF) {
                ASTNode node = parseDeclaration();
                return node;

            } else if (tokens.peek().getTokenType() == TokenType.IDENTIFIER) {
                ASTNode node = parseIdentifier();
            }
        }
        return null;
    }

    /**
     * Parses basic declaration of variables and functions
     *
     * @return ASTNode contains the declaration
     * @throws CompilerException
     */
    private ASTNode parseDeclaration() throws CompilerException {
        Token typeDef, assignment;
        if ((typeDef = found(TokenTypeGroup.TYPEDEF)) != null) { // int ...
            Token identifier = expect(TokenType.IDENTIFIER);

            if ((assignment = found(TokenTypeGroup.ASSIGNMENT_OPERATOR)) != null) { // int a = ...
                if (getIdentifierInScope((String) identifier.getValue()) == null) {
                    ASTVariable variable = createASTVariable(typeDef.getTokenType(), identifier.getLocation(), (String) identifier.getValue(), 0);
                    setVariableInScope(variable);
                    expressionStack.push(variable);
                    operatorStack.push(assignment);
                    ASTNode node = parseExpression();
                    return node;
                } else {
                    throw new CompilerException(String.format("Identifier '%s' is already in use.", (String) identifier.getValue()), identifier.getLocation(), source);
                }

            } else if (found(TokenType.OPEN_PARENTHESES) != null) { // int a( ...
                List<ASTNode> parameters = new ArrayList<>();
                while (tokens.peek().getTokenType() != TokenType.CLOSE_PARENTHESES) {
                    parameters.add(parseDeclaration());
                    if (tokens.peek().getTokenType() != TokenType.CLOSE_PARENTHESES) {
                        expect(TokenType.COMMA);
                    }
                }
                expect(TokenType.CLOSE_PARENTHESES);
                ASTCompoundStatement statement = parseCompoundStatement();
                return new ASTFunction(typeDef.getLocation(), (String) identifier.getValue(), parameters, statement, typeDef.getTokenType());
            }

            if (getIdentifierInScope((String) identifier.getValue()) == null) {
                ASTVariable variable = createASTVariable(typeDef.getTokenType(), identifier.getLocation(), (String) identifier.getValue(), 0);
                setVariableInScope(variable);
                return variable;
            } else {
                throw new CompilerException(String.format("Identifier '%s' is already in use.", (String) identifier.getValue()), identifier.getLocation(), source);
            }
        }

        throw new CompilerException("function parseDeclaration is missing something");
    }

    /**
     * Parses statements that starts with an identifier, can be variable or function.
     *
     * @return returns variable with expression or function call (TODO(Johan): implement function handling)
     * @throws CompilerException
     */
    private ASTNode parseIdentifier() throws CompilerException {
        Token token = expect(TokenType.IDENTIFIER);

        ASTNode node = getIdentifierInScope((String) token.getValue());
        if (node != null && (node instanceof ASTVariable)) {
            ASTVariable variable = (ASTVariable) node;
            Token assignment = found(TokenTypeGroup.ASSIGNMENT_OPERATOR);
            if (assignment != null) { // a = ...
                expressionStack.push(variable);
                operatorStack.push(assignment);
                return parseExpression();
            }
        } else {
            throw new CompilerException(String.format("Can not resolve identifier (%s)", (String) token.getValue()), token.getLocation(), source);
        }

        throw new CompilerException("function parseIdentifier is missing something");
    }

    /**
     * Parses blocks of statements inside braces.
     *
     * @return ASTCompoundStatement
     * @throws CompilerException
     */
    private ASTCompoundStatement parseCompoundStatement() throws CompilerException {
        Token openingBraceToken = expect(TokenType.OPEN_BRACE);

        List<ASTNode> statements = new ArrayList<>();
        while (tokens.peek().getTokenType() != TokenType.CLOSE_BRACE) {
            statements.add(parseStatement());
            expect(TokenType.END_OF_STATEMENT);
        }
        tokens.poll(); // Get rid of CLOSE_BRACE
        if (statements.size() == 0) {
            throw new CompilerException("Empty compound statement", openingBraceToken.getLocation(), source);
        }
        return new ASTCompoundStatement(openingBraceToken.getLocation(), statements);
    }

    /**
     * Parses all types of expressions
     *
     * @return ASTNode node to the root of the AST tree.
     * @throws CompilerException
     */
    private ASTNode parseExpression() throws CompilerException {
        int parentheses = 0;
        while (!tokens.isEmpty()) {
            Token token; // = tokens.peek();
            if ((token = found(TokenType.OPEN_PARENTHESES)) != null) {
                ++parentheses;
                operatorStack.push(token);

            } else if ((token = found(TokenTypeGroup.TYPEDEF_VALUE)) != null) {
                expressionStack.push(new ASTLiteral(token.getTokenType(), token.getLocation(), token.getValue()));

            } else if (parentheses > 0 && (token = found(TokenType.CLOSE_PARENTHESES)) != null) {
                TokenType tokSym = token.getTokenType();
                while (operatorStack.peek().getTokenType() != TokenType.OPEN_PARENTHESES &&
                        (operatorStack.peek().getTokenType().getPrecedence() > tokSym.getPrecedence() ||
                                (operatorStack.peek().getTokenType().getPrecedence() == tokSym.getPrecedence() &&
                                        !operatorStack.peek().getTokenType().isRightAssociative() &&
                                        !tokSym.isRightAssociative()))) {

                    pushOperatorOnExpressionStack();
                }
                operatorStack.pop(); // pops OPEN_PARENTHESES

            } else if (tokens.peek().getTokenType().isOperator()) {
                token = tokens.poll();
                TokenType tokSym = token.getTokenType();
                while (operatorStack.size() > 0 &&
                        (operatorStack.peek().getTokenType().getPrecedence() > tokSym.getPrecedence() ||
                                (operatorStack.peek().getTokenType().getPrecedence() == tokSym.getPrecedence() &&
                                        (!operatorStack.peek().getTokenType().isRightAssociative() &&
                                                !tokSym.isRightAssociative())))) {

                    pushOperatorOnExpressionStack();
                }
                operatorStack.push(token);

            } else {
                // bit strange here, expression counts on that whatever it can't handle is handled directly after this.
                break;
            }
        }

        while (operatorStack.size() > 0) {
            pushOperatorOnExpressionStack();
        }

        typeCheck(expressionStack.peek());
        return expressionStack.peek();
    }

    /**
     * Pops the operator from the operator stack and 1 or 2 expressions depending if the operator
     * is unary or binary.
     *
     * @throws CompilerException exception if the stacks differ from what is expected.
     */
    private void pushOperatorOnExpressionStack() throws CompilerException {
        Token operator = operatorStack.pop();
        if (operator.getTokenType().isUnaryOperator()) {
            if (expressionStack.size() < 1) {
                throw new CompilerException("Expected operand", operator.getLocation(), source);
            }
            ASTNode node = expressionStack.pop();
            if (node.getType().isOperator()) {
                throw new CompilerException("Expected operand", operator.getLocation(), source);
            }
            expressionStack.push(new ASTUnaryOperator(operator.getTokenType(), operator.getLocation(), node));

        } else {
            if (expressionStack.size() < 2) {
                throw new CompilerException("Expected operand", operator.getLocation(), source);
            }
            // Careful, observe order
            ASTNode rightNode = expressionStack.pop();
            ASTNode leftNode = expressionStack.pop();

            // NOTE(Johan): I think this is a "neat" solution, but we'll see if it will work later on.
            if (operator.getTokenType() == TokenType.BINOP_DIV) {
                ASTLiteral result = (ASTLiteral) new Expression(rightNode).solve();
                if (result.isZero()) {
                    throw new CompilerException("Literal divide by zero", rightNode.getLocation(), source);
                }
            }
            expressionStack.push(new ASTBinaryOperator(operator.getTokenType(), operator.getLocation(), leftNode, rightNode));
        }
    }

    /**
     * Type checks the AST tree containing the expression.
     *
     * @param node From where to start the type checking
     * @return returns the valid type unless error
     * @throws CompilerException raises message type mismatch if error
     */
    private TokenType typeCheck(ASTNode node) throws CompilerException {
        if (node instanceof ASTLiteral || node instanceof ASTVariable) {
            return node.getType();
        } else if (node instanceof ASTUnaryOperator) {
            return typeCheck(((ASTUnaryOperator) node).getNode());
        } else if (node instanceof ASTBinaryOperator) {
            TokenType leftNode = typeCheck(((ASTBinaryOperator) node).getLeftNode());
            TokenType rightNode = typeCheck(((ASTBinaryOperator) node).getRightNode());
            if (leftNode == rightNode) {
                return leftNode;
            } else {
                throw new CompilerException("Type mismatch", node.getLocation(), source);
            }
        }
        throw new CompilerException("function typeCheck is missing something", node.getLocation(), source);
    }

    /**
     * Returns the next token in queue if it matches tokenType.
     *
     * @param tokenType
     * @return Token or null if no match
     * @throws CompilerException if unexpected end of tokens
     */
    private Token found(TokenType tokenType) throws CompilerException {
        if (tokens.isEmpty()) {
            throw new CompilerException("Unexpected end of file");
        }

        if (tokens.peek().getTokenType() == tokenType) {
            return tokens.poll();
        } else {
            return null;
        }
    }

    /**
     * Returns the next token in queue if it matches tokenTypeGroup.
     *
     * @param tokenTypeGroup
     * @return Token or null if no match
     * @throws CompilerException if unexpected end of tokens
     */
    private Token found(TokenTypeGroup tokenTypeGroup) throws CompilerException {
        if (tokens.isEmpty()) {
            throw new CompilerException("Unexpected end of file");
        }

        if (tokens.peek().getTokenType().getTokenTypeGroup() == tokenTypeGroup) {
            return tokens.poll();
        } else {
            return null;
        }
    }


    /**
     * Expects next token in queue to match the tokenType.
     *
     * @param tokenType to match
     * @return TokenType
     * @throws CompilerException if unexpected end of tokens or tokenType not matching.
     */
    private Token expect(TokenType tokenType) throws CompilerException {
        if (tokens.isEmpty()) {
            throw new CompilerException("Unexpected end of file");
        }

        Token token = tokens.poll();
        if (token.getTokenType() != tokenType) {
            throw new CompilerException(String.format("Expected '%s'", tokenType.getSymbol()), token.getLocation(), source);
        }
        return token;
    }

    /**
     * Expects next token in queue to match the tokenType.
     *
     * @param tokenTypeGroup to match
     * @return TokenType
     * @throws CompilerException if unexpected end of tokens or tokenTypeGroup not matching.
     */
    private Token expect(TokenTypeGroup tokenTypeGroup) throws CompilerException {
        if (tokens.isEmpty()) {
            throw new CompilerException("Unexpected end of file");
        }

        Token token = tokens.poll();
        if (token.getTokenType().getTokenTypeGroup() != tokenTypeGroup) {
            throw new CompilerException(String.format("Expected '%s'", tokenTypeGroup.toString()), token.getLocation());
        }
        return token;
    }


    private ASTNode getIdentifierInScope(String identifier) {
        // TODO(Johan): variable scope
        return (ASTNode) identifierList.get(identifier);
    }

    private void setVariableInScope(ASTVariable variable) {
        // TODO(Johan): variable scope
        identifierList.put(variable.getName(), variable);
    }

    /**
     * Helper functions that takes a typedef keyword and converts it to corresponding typedef.
     *
     * @param tokenType keyword type
     * @param location  location of the token in the source.
     * @param name      variable name.
     * @param value     initial value of the variable.
     * @return ASTVariable
     * @throws CompilerException if the typedef is unsupported
     */
    private ASTVariable createASTVariable(TokenType tokenType, Location location, String name, Object value) throws CompilerException {
        if (tokenType == TokenType.INT) {
            return new ASTVariable(TokenType.TYPEDEF_INT, location, name, value);
        } else if (tokenType == TokenType.FLOAT) {
            return new ASTVariable(TokenType.TYPEDEF_FLOAT, location, name, value);
        } else if (tokenType == TokenType.DOUBLE) {
            return new ASTVariable(TokenType.TYPEDEF_DOUBLE, location, name, value);
        } else if (tokenType == TokenType.STRING) {
            return new ASTVariable(TokenType.TYPEDEF_STRING, location, name, value);
        }
        throw new CompilerException("Unsupported TYPEDEF in createASTVariable");
    }

    public ASTNode getAstRootNode() {
        return astRootNode;
    }
}
