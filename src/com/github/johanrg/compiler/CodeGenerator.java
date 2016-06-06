package com.github.johanrg.compiler;

import com.github.johanrg.ast.ASTNode;

/**
 * @author Johan Gustafsson
 * @since 2016-06-04
 */
public class CodeGenerator {
    private final ASTNode astRootNode;

    public CodeGenerator(ASTNode astRootNode) {
        this.astRootNode = astRootNode;
    }
}
