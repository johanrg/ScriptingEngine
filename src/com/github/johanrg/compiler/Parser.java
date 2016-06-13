package com.github.johanrg.compiler;

import com.github.johanrg.ast.*;

import java.util.*;
import java.util.stream.Collectors;

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
    private Stack<Token> operatorStack = new Stack<>();
    private Stack<ASTNode> expressionStack = new Stack<>();
    private final Map<String, ASTVariable> variableList = new HashMap<>();

    public Parser(Lexer lexer) throws CompilerException {
        this.tokens = lexer.getTokens();
        this.source = lexer.getSource();

        parseStatement();
        astRootNode = parseExpression();
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

    private void parseStatement() throws CompilerException {
        while (!tokens.isEmpty()) {
            Token token = tokens.poll();
        }
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
     * This parses all types of expressions
     *
     * @return ASTNode node to the root of the AST tree.
     * @throws CompilerException
     */
    private ASTNode parseExpression() throws CompilerException {
        while (!tokens.isEmpty()) {
            Token token = tokens.poll();
            if (token.getTokenType() == TokenType.OPEN_PARENTHESES) {
                operatorStack.push(token);

            } else if (token.getTokenType().isNumber()) {
                expressionStack.push(new ASTLiteral(token.getTokenType(), token.getLocation(), token.getValue()));

            } else if (token.getTokenType() == TokenType.CLOSE_PARENTHESES) {
                TokenType tokSym = token.getTokenType();
                while (operatorStack.peek().getTokenType() != TokenType.OPEN_PARENTHESES &&
                        (operatorStack.peek().getTokenType().getPrecedence() > tokSym.getPrecedence() ||
                                (operatorStack.peek().getTokenType().getPrecedence() == tokSym.getPrecedence() &&
                                        !operatorStack.peek().getTokenType().isRightAssociative() &&
                                        !tokSym.isRightAssociative()))) {

                    pushOperatorOnExpressionStack();
                }
                operatorStack.pop(); // pops OPEN_PARENTHESES

            } else if (token.getTokenType().isOperator()) {
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
                throw new CompilerException("Syntax error", token.getLocation(), source);
            }
        }

        while (operatorStack.size() > 0) {
            pushOperatorOnExpressionStack();
        }

        typeCheck(expressionStack.peek());
        return expressionStack.peek();
    }

    public ASTNode getAstRootNode() {
        return astRootNode;
    }
}
