/*
package fx;

import java.math.BigDecimal;

public class CalculatorOLD {
    public static void main(String[] args) throws Exception {
        SyntaxTree tree = SyntaxTree.parse("1.1");
        System.out.println(tree.eval().toString());
    }
    public String eval(String input) throws Exception {
        return SyntaxTree.parse(input).eval().toString();
    }
}

*/
/**
 * expr     ::= addi
 * addi     ::= mult | ( addi ( "+" | "-" ) mult )
 * mult     ::= prim | ( mult ( "*" | "/" ) prim )
 * prim     ::= cons | func
 * func     ::= ( [NAME] "(" expr ")" )
 * cons     ::= NUMB "e" NUMB | "e" | "π"
 *//*


class SyntaxTree {
    final static String PI = "π";
    final static String E = "e";
    final static boolean debug = false;

    protected Node root;

    SyntaxTree(Node root) {
        this.root = root;
    }

    protected static void debugPrintln(String info) {
        if (debug) {
            System.out.println("Debug: " + info);
        }
    }

    public String toString() {
        return root.toString();
    }

    public BigDecimal eval() {
        return root.eval();
    }

    protected static int index(String s, char c) {
        int paren = 0;
        for (int i=s.length()-1; i>=0; i--) {
            if (s.charAt(i) == ')') {
                paren ++;
                continue;
            }
            if (s.charAt(i) == '(') {
                paren --;
                continue;
            }
            if (paren == 0 && s.charAt(i) == c) {
                return i;
            }
        }
        return -1;
    }

    public static SyntaxTree parse(String s) throws Exception {
        return new SyntaxTree(parseExpr(s));
    }

    protected static Node parseExpr(String s) throws Exception {
        debugPrintln("parseExpr: " + s);
        return parseAddi(s);
    }

    protected static Node parseAddi(String s) throws Exception {
        debugPrintln("parseAddi: " + s);
        int lastPlus = index(s, '+'), lastMinu = index(s, '-');
        if (lastPlus == -1 && lastMinu == -1) {
            return parseMult(s);
        }
        if (lastPlus > lastMinu) {
            return new Plus(parseAddi(s.substring(0, lastPlus)), parseMult(s.substring(lastPlus+1)));
        }
        else {
            return new Minus(parseAddi(s.substring(0, lastMinu)), parseMult(s.substring(lastMinu+1)));
        }
    }

    protected static Node parseMult(String s) throws Exception {
        debugPrintln("parseMult: " + s);
        int lastMult = index(s, '*'), lastDivi = index(s, '/');
        if (lastMult == -1 && lastDivi == -1) {
            return parsePrim(s);
        }
        if (lastMult > lastDivi) {
            return new Multiply(parseMult(s.substring(0, lastMult)), parsePrim(s.substring(lastMult+1)));
        }
        else {
            return new Divide(parseMult(s.substring(0, lastDivi)), parsePrim(s.substring(lastDivi+1)));
        }
    }

    protected static Node parsePrim(String s) throws Exception {
        debugPrintln("parsePrim: " + s);
        if (s.length() == 0) {
            return new Null();
        }

        for (int i=0; i<s.length(); i++) {
            if (! isConst(s.charAt(i))) {
                return parseFunc(s);
            }
        }
        return parseCons(s);
    }

    protected static Node parseFunc(String s) throws Exception {
        debugPrintln("parseFunc: " + s);
        if (isAlpha(s.charAt(0))) {
            // is func
            String funcName = s.substring(0, 3);
            assert s.charAt(3) == '(' && s.charAt(s.length()-1) == ')';
            Node val = parseExpr(s.substring(4, s.length()-1));
            if (funcName.equals("sin")) {
                return new Sin(val);
            }
            else if (funcName.equals("cos")) {
                return new Cos(val);
            }
            else if (funcName.equals("tan")) {
                return new Tan(val);
            }
            else {
                throw new Exception("Invalid function name.");
            }
        }
        else {
            if (s.charAt(0) != '(' || s.charAt(s.length()-1) != ')') {
                throw new Exception("Invalid paren.");
            }
            return parseExpr(s.substring(1, s.length()-1));
        }
    }

    protected static Node parseCons(String s) throws Exception {
        debugPrintln("parseCons: " + s);
        if (s.equals(E)) {
            return new E();
        }
        if (s.equals(PI)) {
            return new Pi();
        }
        int lastE = index(s, 'e'), lastPI = index(s, 'π');
        if (lastE == -1 && lastPI == -1) {
            return new Number(s);
        }
        if (lastE == s.length()-1) {
            return new Multiply(parseCons(s.substring(0, s.length()-1)), new E());
        }
        if (lastPI == s.length()-1) {
            return new Multiply(parseCons(s.substring(0, s.length()-1)), new Pi());
        }
        if (lastE == 0) {
            return new Multiply(new E(), parseCons(s.substring(1)));
        }
        if (lastE > lastPI) {
            return new Exp(parseCons(s.substring(0, lastE)), parseCons(s.substring(lastE+1)));
        }
        return new Multiply(parseCons(s.substring(0, lastPI)), new Number(s.substring(lastPI+1)));
    }

    protected static boolean isConst(char ch) {
        return ch == 'e' || ch == 'π' || isNumber(ch);
    }

    protected static boolean isNumber(char ch) {
        return (ch == '.') || (ch >= '0' && ch <= '9');
    }

    // protected static boolean isBinOp(char ch) {
    //     return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == 'e';
    // }

    protected static boolean isAlpha(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    // protected static boolean isParen(char ch) {
    //     return ch == '(' || ch == ')';
    // }
}

abstract class Node {
    protected String name;
    public String getName() { return name; }
    abstract public String toString();
    abstract public BigDecimal eval();
}

class Null extends Node {
    Null() {
        name = "";
    }
    public String toString() { return name ;}
    public BigDecimal eval() { return BigDecimal.ZERO; }
}

class Number extends Node {
    private BigDecimal val;
    Number(String num) {
        name = num;
        val = new BigDecimal(num);
    }
    Number(BigDecimal num) {
        val = num;
        name = val.toString();
    }
    protected Number(String name, BigDecimal num) {
        this.name = name;
        this.val = num;
    }
    public String toString() { return name; }
    public BigDecimal eval() { return val; }
}

class Pi extends Number {
    Pi() {
        super("π", BigDecimal.valueOf(Math.PI));
    }
}

class E extends Number {
    E() {
        super("e", BigDecimal.valueOf(Math.E));
    }
}

abstract class BinOp extends Node {
    protected Node lhs, rhs;
    BinOp(Node lhs, Node rhs, boolean err) throws Exception {
        if (err && (lhs instanceof Null || rhs instanceof Null)) {
            throw new Exception("Invalid input.");
        }
        this.lhs = lhs;
        this.rhs = rhs;
    }
    public String toString() {
        return "(" + lhs.toString() + name + rhs.toString() + ")";
    }
}

class Plus extends BinOp {
    Plus(Node lhs, Node rhs) throws Exception {
        super(lhs, rhs, true);
        name = "+";
    }
    public BigDecimal eval() {
        return lhs.eval().add(rhs.eval());
    }
}

class Minus extends BinOp {
    Minus(Node lhs, Node rhs) throws Exception {
        super(lhs, rhs, true);
        name = "-";
    }
    public BigDecimal eval() {
        return lhs.eval().subtract(rhs.eval());
    }
}

class Multiply extends BinOp {
    Multiply(Node lhs, Node rhs) throws Exception {
        super(lhs, rhs, true);
        name = "*";
    }
    public BigDecimal eval() {
        return lhs.eval().multiply(rhs.eval());
    }
}

class Divide extends BinOp {
    Divide(Node lhs, Node rhs) throws Exception {
        super(lhs, rhs, true);
        name = "/";
    }
    public BigDecimal eval() {
        return lhs.eval().divide(rhs.eval());
    }
}

class Exp extends BinOp {
    Exp(Node lhs, Node rhs) throws Exception {
        super(lhs, rhs, false);
        name = "e";
    }
    public BigDecimal eval() {
        if (rhs instanceof Null) {
            return lhs.eval().multiply(BigDecimal.valueOf(Math.E));
        }
        else {
            return lhs.eval().multiply(BigDecimal.valueOf(Math.pow(10.0, rhs.eval().doubleValue())));
        }
    }
}

abstract class Function extends Node {
    protected Node val;
    Function(Node val) {
        this.val = val;
    }
    public String toString() {
        return name + "(" + val.toString() + ")";
    }
}

class Sin extends Function {
    Sin(Node val) {
        super(val);
        name = "sin";
    }
    public BigDecimal eval() {
        return BigDecimal.valueOf(Math.sin(val.eval().doubleValue()));
    }
}

class Cos extends Function {
    Cos(Node val) {
        super(val);
        name = "cos";
    }
    public BigDecimal eval() {
        return BigDecimal.valueOf(Math.cos(val.eval().doubleValue()));
    }
}

class Tan extends Function {
    Tan(Node val) {
        super(val);
        name = "tan";
    }
    public BigDecimal eval() {
        return BigDecimal.valueOf(Math.tan(val.eval().doubleValue()));
    }
}
*/
