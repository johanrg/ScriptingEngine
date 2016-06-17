package com.github.johanrg.compiler;

import com.github.johanrg.ast.*;

/**
 * @author Johan Gustafsson
 * @since 6/16/2016.
 */
public class Compiler {
    private final ASTNode rootNode;

    public Compiler(ASTNode rootNode) {
        this.rootNode = rootNode;
        printAST(rootNode, "");
    }

    /**
     * This is just to get some debug information.
     *
     * @param node
     * @return
     */
    private ASTNode printAST(ASTNode node, String depth) {
        if (node == null) return null;

        if (node instanceof ASTLiteral) {
            return node;

        } else if (node instanceof ASTVariable) {
            System.out.printf(depth + "VARIABLE %s: %s\n", ((ASTVariable) node).getName(), node.getType());
            return node;

        } else if (node instanceof ASTBinaryOperator) {
            ASTNode leftNode = printAST(((ASTBinaryOperator) node).getLeftNode(), depth);
            ASTNode rightNode = printAST(((ASTBinaryOperator) node).getRightNode(), depth);

            if (rightNode == null)
                rightNode = new ASTLiteral(TokenType.TYPEDEF_STRING, new Location(0, 0), "<- A");

            switch (node.getType()) {
                case BINOP_ADD:
                    System.out.printf(depth + "ADD: %s, %s -> A\n", leftNode, rightNode);
                    break;

                case BINOP_SUBTRACT:
                    System.out.printf(depth + "SUBTRACT: %s, %s -> A\n", leftNode, rightNode);
                    break;

                case BINOP_MUL:
                    System.out.printf(depth + "MUL: %s, %s -> A\n", leftNode, rightNode);
                    break;

                case BINOP_DIV:
                    System.out.printf(depth + "DIV: %s, %s -> A\n", leftNode, rightNode);
                    break;

                case BINOP_MOD:
                    System.out.printf(depth + "MOD: %s, %s -> A\n", leftNode, rightNode);
                    break;

                case BINOP_EXPONENT:
                    System.out.printf(depth + "EXPONENT: %s, %s -> A\n", leftNode, rightNode);
                    break;

                case ASSIGNMENT:
                    System.out.printf(depth + "ASSIGNMENT %s : %s | %s\n", node.getType().getSymbol(), leftNode, rightNode);
            }

            return null;
        } else if (node instanceof ASTCompoundStatement) {
            System.out.println(depth + "{");
            ((ASTCompoundStatement) node).getStatements().forEach(N -> printAST(N, depth + "\t"));
            System.out.println(depth + "}");
        } else if (node instanceof ASTFunction) {
            System.out.printf(depth + "FUNCTION DECLARATION: %s -> %s\n", ((ASTFunction) node).getName(), node.getType().getSymbol());
            System.out.println(depth + "(");
            ((ASTFunction) node).getParameters().forEach(N -> printAST(N, depth + "\t"));
            System.out.println(depth + ")");
            System.out.println(depth + "{");
            ((ASTFunction) node).getCompoundStatement().getStatements().forEach(N -> printAST(N, depth + "\t"));
            System.out.println(depth + "}");
        } else if (node instanceof ASTUnaryOperator) {

            ASTNode unaryNode = printAST(((ASTUnaryOperator) node).getNode(), depth);
            if (node.getType() == TokenType.UNARY_MINUS) {
                System.out.println(depth + "NEG A");
            }
            return unaryNode;
        }
        return null;
    }
}
