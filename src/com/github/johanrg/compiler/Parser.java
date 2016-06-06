package com.github.johanrg.compiler;

import com.github.johanrg.ast.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * The parser builds the complete AST tree for the scripting language.
 *
 * @author Johan Gustafsson
 * @since 2016-06-04
 */
public class Parser {
    private final List<Token> tokens;
    private final ASTNode astRootNode;
    private final Map<String, ASTVariable> variableList = new HashMap<>();

    public Parser(List<Token> tokens) throws CompilerException {
        this.tokens = tokens;
        astRootNode = parseExpression(0);
    }

    /**
     * Type checks the AST tree containing the expression.
     *
     * @param node From where to start the type checking
     * @return returns the valid type unless error
     * @throws CompilerException raises message type mismatch if error
     */
    private TokenType typeCheck(ASTNode node) throws CompilerException {
        if (node instanceof ASTLiteral) {
            return node.getType();
        } else if (node instanceof ASTUnaryOperator) {
            return typeCheck(((ASTUnaryOperator) node).getNode());
        } else if (node instanceof ASTBinaryOperator) {
            TokenType leftNode = typeCheck(((ASTBinaryOperator) node).getLeftNode());
            TokenType rightNode = typeCheck(((ASTBinaryOperator) node).getRightNode());
            if (leftNode == rightNode) {
                return leftNode;
            } else {
                throw new CompilerException("Type mismatch", node.getLocation());
            }
        }
        throw new CompilerException("function typeCheck is missing something", node.getLocation());
    }

    /**
     * Pops the operator from the operator stack and 1 or 2 expressions depending if the operator
     * is unary or binary.
     * @param operatorStack stack of operators
     * @param expressionStack stack of expressions, literals or operator/operand(s)
     * @throws CompilerException exception if the stacks differ from what is expected.
     */
    private void pushOperatorOnExpressionStack(Stack<Token> operatorStack, Stack<ASTNode> expressionStack) throws CompilerException {
        Token operator = operatorStack.pop();

        if (operator.getSymbolType().isUnaryOperator()) {
            if (expressionStack.size() < 1) {
                throw new CompilerException("Expected operand", operator.getLocation());
            }
            ASTNode node = expressionStack.pop();
            if (node.getType().isOperator()) {
                throw new CompilerException("Expected operand", operator.getLocation());
            }
            expressionStack.push(new ASTUnaryOperator(operator.getSymbolType(), operator.getLocation(), node));

        } else {
            if (expressionStack.size() < 2) {
                throw new CompilerException("Expected operand", operator.getLocation());
            }
            // Careful, observe order
            ASTNode rightNode = expressionStack.pop();
            ASTNode leftNode = expressionStack.pop();

            // NOTE(Johan): I think this is a "neat" solution, but we'll see if it will work later on.
            if (operator.getSymbolType() == TokenType.BINOP_DIV) {
                ASTLiteral result = (ASTLiteral) new Expression(rightNode).solve();
                if (result.isZero()) {
                    throw new CompilerException("Literal divide by zero", rightNode.getLocation());
                }
            }
            expressionStack.push(new ASTBinaryOperator(operator.getSymbolType(), operator.getLocation(), leftNode, rightNode));
        }
    }

    /**
     * This parses all types of expressions
     *
     * @param index position to start parsing
     * @return ASTNode node to the root of the AST tree.
     * @throws CompilerException
     */
    private ASTNode parseExpression(int index) throws CompilerException {
        Stack<Token> operatorStack = new Stack<>();
        Stack<ASTNode> expressionStack = new Stack<>();

        for (Token token : tokens.stream().skip(index).limit(tokens.size() - index).collect(Collectors.toList())) {
            if (token.getSymbolType() == TokenType.OPEN_PARENTHESES) {
                operatorStack.push(token);

            } else if (token.getSymbolType().isNumber()) {
                expressionStack.push(new ASTLiteral(token.getSymbolType(), token.getLocation(), token.getValue()));

            } else if (token.getSymbolType() == TokenType.CLOSE_PARENTHESES) {
                TokenType tokSym = token.getSymbolType();
                while (operatorStack.peek().getSymbolType() != TokenType.OPEN_PARENTHESES &&
                        (operatorStack.peek().getSymbolType().getPrecedence() > tokSym.getPrecedence() ||
                                (operatorStack.peek().getSymbolType().getPrecedence() == tokSym.getPrecedence() &&
                                        !operatorStack.peek().getSymbolType().isRightAssociative() &&
                                        !tokSym.isRightAssociative()))) {

                    pushOperatorOnExpressionStack(operatorStack, expressionStack);
                }
                operatorStack.pop(); // pops OPEN_PARENTHESES

            } else if (token.getSymbolType().isOperator()) {
                TokenType tokSym = token.getSymbolType();
                while (operatorStack.size() > 0 &&
                        (operatorStack.peek().getSymbolType().getPrecedence() > tokSym.getPrecedence() ||
                                (operatorStack.peek().getSymbolType().getPrecedence() == tokSym.getPrecedence() &&
                                        (!operatorStack.peek().getSymbolType().isRightAssociative() &&
                                                !tokSym.isRightAssociative())))) {

                    pushOperatorOnExpressionStack(operatorStack, expressionStack);
                }
                operatorStack.push(token);

            } else {
                throw new CompilerException("Syntax error", token.getLocation());
            }
        }

        while (operatorStack.size() > 0) {
            pushOperatorOnExpressionStack(operatorStack, expressionStack);
        }

        typeCheck(expressionStack.peek());
        return expressionStack.peek();
    }

    public ASTNode getAstRootNode() {
        return astRootNode;
    }
}
