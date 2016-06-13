package com.github.johanrg.compiler;

import com.github.johanrg.ast.ASTNode;
import com.github.johanrg.ast.ASTBinaryOperator;
import com.github.johanrg.ast.ASTLiteral;
import com.github.johanrg.ast.ASTUnaryOperator;

/**
 * Just to have something to show for now. This will most likely not be part of the final thing.. but well see.
 *
 * @author Johan Gustafsson
 * @since 2016-06-04
 */
public class Expression {
    private final ASTNode astRootNode;

    public Expression(ASTNode astRootNode) {
        this.astRootNode = astRootNode;
    }

    /**
     * Helper to solve the AST expression without having to know the root node.
     *
     * @return ASTNode
     */
    public ASTNode solve() throws CompilerException {
        return solveExpression(astRootNode);
    }

    /**
     * Solves the AST expression, is currently used for checking divide by zero on literal values
     * and as a calculator to test AST expressions.
     *
     * OBS! Can only solve literal expressions!
     *
     * @param node The starting node to solve the expression, can be anywhere in expression tree.
     * @return
     * @throws CompilerException
     */
    private ASTLiteral solveExpression(ASTNode node) throws CompilerException {
        if (node instanceof ASTLiteral) {
            return (ASTLiteral) node;

        } else if (node instanceof ASTBinaryOperator) {
            ASTLiteral leftNode = solveExpression(((ASTBinaryOperator) node).getLeftNode());
            ASTLiteral rightNode = solveExpression(((ASTBinaryOperator) node).getRightNode());

            Object result;
            switch (node.getType()) {
                case BINOP_ADD:
                    result = add(leftNode, rightNode);
                    break;

                case BINOP_SUBTRACT:
                    result = subtract(leftNode, rightNode);
                    break;

                case BINOP_MUL:
                    result = multiply(leftNode, rightNode);
                    break;

                case BINOP_DIV:
                    result = divide(leftNode, rightNode);
                    break;

                case BINOP_MOD:
                    result = modulus(leftNode, rightNode);
                    break;

                case BINOP_EXPONENT:
                    result = power(leftNode, rightNode);
                    break;

                default:
                    throw new CompilerException("solveExpression(): end of switch, not handled.");
            }

            return new ASTLiteral(leftNode.getType(), leftNode.getLocation(), result);

        } else if (node instanceof ASTUnaryOperator) {
            ASTLiteral unaryNode = solveExpression(((ASTUnaryOperator) node).getNode());

            if (node.getType() == TokenType.UNARY_MINUS) {
                if (unaryNode.getType() == TokenType.TYPEDEF_INT) {
                    Object value = multiply(unaryNode, new ASTLiteral(TokenType.TYPEDEF_INT, null, -1));
                    return new ASTLiteral(TokenType.TYPEDEF_INT, unaryNode.getLocation(), value);
                } else if (unaryNode.getType() == TokenType.TYPEDEF_FLOAT) {
                    Object value = multiply(unaryNode, new ASTLiteral(TokenType.TYPEDEF_FLOAT, null, -1.f));
                    return new ASTLiteral(TokenType.TYPEDEF_FLOAT, unaryNode.getLocation(), value);
                } else if (unaryNode.getType() == TokenType.TYPEDEF_DOUBLE) {
                    Object value = multiply(unaryNode, new ASTLiteral(TokenType.TYPEDEF_DOUBLE, null, -1.0));
                    return new ASTLiteral(TokenType.TYPEDEF_DOUBLE, unaryNode.getLocation(), value);
                }
            } else if (node.getType() == TokenType.UNARY_PLUS) {
                // NOTE(Johan): Ignoring and passing value, am I missing anything?
                return unaryNode;
            }

        }
        throw new CompilerException("solveExpression(): end of function, not handled.");
    }

    private Object add(ASTLiteral lhs, ASTLiteral rhs) throws CompilerException {
        Object result;
        if (lhs.getType().equals(rhs.getType())) {
            if (lhs.getType() == TokenType.TYPEDEF_INT) {
                result = (Integer) lhs.getValue() + (Integer) rhs.getValue();
            } else if (lhs.getType() == TokenType.TYPEDEF_FLOAT) {
                result = (Float) lhs.getValue() + (Float) rhs.getValue();
            } else if (lhs.getType() == TokenType.TYPEDEF_DOUBLE) {
                result = (Double) lhs.getValue() + (Double) rhs.getValue();
            } else {
                throw new CompilerException("Invalid type", lhs.getLocation());
            }
        } else {
            throw new CompilerException("Multiply must have two operands of the same data type", lhs.getLocation());
        }

        return result;
    }

    private Object subtract(ASTLiteral lhs, ASTLiteral rhs) throws CompilerException {
        Object result;
        if (lhs.getType().equals(rhs.getType())) {
            if (lhs.getType() == TokenType.TYPEDEF_INT) {
                result = (Integer) lhs.getValue() - (Integer) rhs.getValue();
            } else if (lhs.getType() == TokenType.TYPEDEF_FLOAT) {
                result = (Float) lhs.getValue() - (Float) rhs.getValue();
            } else if (lhs.getType() == TokenType.TYPEDEF_DOUBLE) {
                result = (Double) lhs.getValue() - (Double) rhs.getValue();
            } else {
                throw new CompilerException("Invalid type", lhs.getLocation());
            }
        } else {
            throw new CompilerException("Multiply must have two operands of the same data type", lhs.getLocation());
        }

        return result;
    }

    private Object multiply(ASTLiteral lhs, ASTLiteral rhs) throws CompilerException {
        Object result;
        if (lhs.getType().equals(rhs.getType())) {
            if (lhs.getType() == TokenType.TYPEDEF_INT) {
                result = (Integer) lhs.getValue() * (Integer) rhs.getValue();
            } else if (lhs.getType() == TokenType.TYPEDEF_FLOAT) {
                result = (Float) lhs.getValue() * (Float) rhs.getValue();
            } else if (lhs.getType() == TokenType.TYPEDEF_DOUBLE) {
                result = (Double) lhs.getValue() * (Double) rhs.getValue();
            } else {
                throw new CompilerException("Invalid type", lhs.getLocation());
            }
        } else {
            throw new CompilerException("Multiply must have two operands of the same data type", lhs.getLocation());
        }

        return result;
    }

    private Object divide(ASTLiteral lhs, ASTLiteral rhs) throws CompilerException {
        if (lhs.getType().equals(rhs.getType())) {
            if (lhs.getType() == TokenType.TYPEDEF_INT) {
                return (Integer) lhs.getValue() / (Integer) rhs.getValue();
            } else if (lhs.getType() == TokenType.TYPEDEF_FLOAT) {
                return (Float) lhs.getValue() / (Float) rhs.getValue();
            } else if (lhs.getType() == TokenType.TYPEDEF_DOUBLE) {
                return (Double) lhs.getValue() / (Double) rhs.getValue();
            } else {
                throw new CompilerException("Invalid type", lhs.getLocation());
            }
        } else {
            throw new CompilerException("Divide must have two operands of the same data type", lhs.getLocation());
        }
    }

    private Integer modulus(ASTLiteral lhs, ASTLiteral rhs) throws CompilerException {
        Object result;
        if (lhs.getType().equals(rhs.getType())) {
            if (lhs.getType() == TokenType.TYPEDEF_INT) {
                return (Integer) lhs.getValue() % (Integer) rhs.getValue();
            } else {
                throw new CompilerException("Invalid type", lhs.getLocation());
            }
        } else {
            throw new CompilerException("Modulus must have two operands of the same data type", lhs.getLocation());
        }
    }

    private Object power(ASTLiteral lhs, ASTLiteral rhs) throws CompilerException {
        if (lhs.getType().equals(rhs.getType())) {
            if (lhs.getType() == TokenType.TYPEDEF_DOUBLE) {
                return Math.pow((Double) lhs.getValue(), (Double) rhs.getValue());
            } else if (lhs.getType() == TokenType.TYPEDEF_INT) {
                return new Double(Math.pow((Integer) lhs.getValue(), (Integer) rhs.getValue())).intValue();
            } else if (lhs.getType() == TokenType.TYPEDEF_FLOAT) {
                return new Double(Math.pow((Float) lhs.getValue(), (Float) rhs.getValue())).floatValue();
            } else {
                throw new CompilerException("Invalid type", lhs.getLocation());
            }
        } else {
            throw new CompilerException("Exponent must have two operands of the same data type", lhs.getLocation());
        }
    }
}
