package com.github.johanrg;

import com.github.johanrg.ast.ASTNode;
import com.github.johanrg.compiler.*;

public class Main {

    public static void main(String[] args) {
        try {
            Lexer p;
            Parser l;
            Expression e;
            ASTNode r;


            p = new Lexer("10 / (5 + (10 - 10) - 5);");
            l = new Parser(p.getTokens());
            e = new Expression(l.getAstRootNode());
            r = e.solve();
            System.out.println(r);

            p = new Lexer("10;");
            l = new Parser(p.getTokens());
            e = new Expression(l.getAstRootNode());
            r = e.solve();
            System.out.println(r);


            p = new Lexer("-10;");
            l = new Parser(p.getTokens());
            e = new Expression(l.getAstRootNode());
            r = e.solve();
            System.out.println(r);

            p = new Lexer("+10;");
            l = new Parser(p.getTokens());
            e = new Expression(l.getAstRootNode());
            r = e.solve();
            System.out.println(r);

            p = new Lexer("((1*2)+3-(-4*5));");
            l = new Parser(p.getTokens());
            e = new Expression(l.getAstRootNode());
            r = e.solve();
            System.out.println(r);

            p = new Lexer("((1.f*2.f)+3.f/(-4.f*5.f));");
            l = new Parser(p.getTokens());
            e = new Expression(l.getAstRootNode());
            r = e.solve();
            System.out.println(r);

            p = new Lexer("(1 + (-2 * 3));");
            l = new Parser(p.getTokens());
            e = new Expression(l.getAstRootNode());
            r = e.solve();
            System.out.println(r);


        } catch (CompilerException e) {
            System.out.println(e.getMessage());
        }
    }
}
