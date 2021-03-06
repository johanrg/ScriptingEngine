package com.github.johanrg;

import com.github.johanrg.ast.ASTNode;
import com.github.johanrg.compiler.*;
import com.github.johanrg.compiler.Compiler;

public class Main {

    public static void main(String[] args) {
            Lexer l;
            Parser p;
            Expression e;
            ASTNode r;

        try {
            StringBuilder sb = new StringBuilder();
/*            sb.append("int a(int b, int d = 0){int x = 0;} int bbb() {int ddd = 0;}");*/
/*            sb.append("int a(int b, int c){int x = 10;}");*/
/*            sb.append("int a = -1;");*/
            sb.append("int a = -(5 * 7 + (44 / 2) - 1 / 2);");
            l = new Lexer(sb.toString());
            p = new Parser(l);
            new Compiler(p.getAstRootNode());
        } catch (CompilerException e1) {
            System.out.println(e1.getMessage());
        }

/*
        try {
            l = new Lexer("");
            p = new Parser(l);
            e = new Expression(p.getAstRootNode());
            r = e.solve();
            System.out.println(r);
        } catch (CompilerException e1) {
            System.out.println(e1.getMessage());
        }

        try {
            l = new Lexer("10 + b;");
            p = new Parser(l);
            e = new Expression(p.getAstRootNode());
            r = e.solve();
            System.out.println(r);
        } catch (CompilerException e1) {
            System.out.println(e1.getMessage());
        }

        try {
            l = new Lexer("10 / (5 + (10 - 10) - 5);");
            p = new Parser(l);
            e = new Expression(p.getAstRootNode());
            r = e.solve();
            System.out.println(r);
        } catch (CompilerException e1) {
            System.out.println(e1.getMessage());
        }

        try {
            l = new Lexer("10;");
            p = new Parser(l);
            e = new Expression(p.getAstRootNode());
            r = e.solve();
            System.out.println(r);
        } catch (CompilerException e1) {
            System.out.println(e1.getMessage());
        }


        try {
            l = new Lexer("-10;");
            p = new Parser(l);
            e = new Expression(p.getAstRootNode());
            r = e.solve();
            System.out.println(r);
        } catch (CompilerException e1) {
            System.out.println(e1.getMessage());
        }

        try {
            l = new Lexer("+10;");
            p = new Parser(l);
            e = new Expression(p.getAstRootNode());
            r = e.solve();
            System.out.println(r);
        } catch (CompilerException e1) {
            System.out.println(e1.getMessage());
        }

        try {
            l = new Lexer("((1*2)+3-(-4*5));");
            p = new Parser(l);
            e = new Expression(p.getAstRootNode());
            r = e.solve();
            System.out.println(r);
        } catch (CompilerException e1) {
            System.out.println(e1.getMessage());
        }

        try {
            l = new Lexer("((1.f*2.f)+3.f/(-4.f*5.f));");
            p = new Parser(l);
            e = new Expression(p.getAstRootNode());
            r = e.solve();
            System.out.println(r);
        } catch (CompilerException e1) {
            System.out.println(e1.getMessage());
        }

        try {
            l = new Lexer("(1 + (-2 * 3));");
            p = new Parser(l);
            e = new Expression(p.getAstRootNode());
            r = e.solve();
            System.out.println(r);
        } catch (CompilerException e1) {
            System.out.println(e1.getMessage());
        }
*/

    }
}
