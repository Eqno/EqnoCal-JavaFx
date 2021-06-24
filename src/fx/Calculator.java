package fx;

import java.math.BigDecimal;

public class Calculator {
    public static void main(String[] args) throws Exception {
        // String expr = "πe-1";
        // String expr = "πe";
        // String expr = "1e+10.1";
        // String expr = "1e+10";
        // String expr = "sqrt(-4.0)";
        // String expr = "sqrt(-(-4.0))";
        // String expr = "sqrt(+4.0)";
        // String expr = "sqrt()";
        String expr = "sqrt(4)!+sqrt(4!)";
        SyntaxTree tree = parse(expr);
        System.out.println(expr + " => " + tree.toString() + " = " + tree.eval().doubleValue());
    }

    public static SyntaxTree parse(String expr) throws Exception {
        return SyntaxTree.parse(expr);
    }
    public static BigDecimal eval(String expr) throws Exception {
        return SyntaxTree.parse(expr).eval();
    }
}

/**
 * expr     ::= addi
 * addi     ::= mult | ( addi ( "+" | "-" ) mult )
 * mult     ::= powe | ( mult ( "*" | "/" ) powe )
 * powe     ::= prim | ( powe "^" prim)
 * prim     ::= cons | func
 * func     ::= ( [NAME] "(" expr ")" ) | ( prim "!")
 * cons     ::= NUMB ( "e" | "e+" | "e-" ) prim | "E" | "π"
 */

class SyntaxTree {
    final static String PI = "π";
    final static String E = "E";
    final static String EXP = "e";
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

    public BigDecimal eval() throws Exception {
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
        if (lastPlus > 0 && s.charAt(lastPlus-1) == 'e') {
            if (lastPlus > 1 && isNumber(s.charAt(lastPlus-2))) {
                lastPlus = -1;
            }
        }
        if (lastMinu > 0 && s.charAt(lastMinu-1) == 'e') {
            if (lastMinu > 1 && isNumber(s.charAt(lastMinu-2))) {
                lastMinu = -1;
            }
        }
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
            return parsePower(s);
        }
        if (lastMult > lastDivi) {
            return new Multiply(parseMult(s.substring(0, lastMult)), parsePower(s.substring(lastMult+1)));
        }
        else {
            return new Divide(parseMult(s.substring(0, lastDivi)), parsePower(s.substring(lastDivi+1)));
        }
    }

    protected static Node parsePower(String s) throws Exception {
        debugPrintln("parsePower: " + s);
        int lastPower = index(s, '^');
        if (lastPower == -1) {
            return parsePrim(s);
        }
        return new Power(parsePower(s.substring(0, lastPower)), parsePrim(s.substring(lastPower+1)));
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
        if (s.charAt(s.length()-1) == '!') {
            // is factorial
            return new Factorial(parsePrim(s.substring(0, s.length()-1)));
        }
        else if (isAlpha(s.charAt(0))) {
            // is simple func
            int pos;
            for (pos=1; pos<s.length(); pos++) {
                if (! isAlpha(s.charAt(pos)))
                {
                    break;
                }
            }
            String funcName = s.substring(0, pos);
            if (s.charAt(pos) != '(' || s.charAt(s.length()-1) != ')') {
                throw new Exception("Invalid syntax.");
            }
            Node val = parseExpr(s.substring(pos+1, s.length()-1));
            if (funcName.equals("sin")) {
                return new Sin(val);
            }
            else if (funcName.equals("cos")) {
                return new Cos(val);
            }
            else if (funcName.equals("tan")) {
                return new Tan(val);
            }
            else if (funcName.equals("arcsin")) {
                return new Arcsin(val);
            }
            else if (funcName.equals("arccos")) {
                return new Arccos(val);
            }
            else if (funcName.equals("arctan")) {
                return new Arctan(val);
            }
            else if (funcName.equals("sqrt")) {
                return new Sqrt(val);
            }
            else if (funcName.equals("lg")) {
                return new Lg(val);
            }
            else if (funcName.equals("ln")) {
                return new Ln(val);
            }
            else {
                throw new Exception("Invalid function name.");
            }
        }
        else {
            // only paren
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
        int lastE = index(s, 'e');
        if (lastE == -1) {
            return new Number(s);
        }
        return new Exp(parseCons(s.substring(0, lastE)), parsePrim(s.substring(lastE+1)));
    }

    protected static boolean isConst(char ch) {
        return ch == 'e' || isUnaryOp(ch) || isNumber(ch);
    }

    protected static boolean isNumber(char ch) {
        return (ch == '.' || ch == 'E' || ch == 'π') || (ch >= '0' && ch <= '9');
    }

    protected static boolean isUnaryOp(char ch) {
        return ch == '+' || ch == '-';
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
    abstract public BigDecimal eval() throws Exception;
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

class EE extends Number {
    EE() {
        super("E", BigDecimal.valueOf(Math.E));
    }
}

abstract class BinOp extends Node {
    protected Node lhs, rhs;
    BinOp(Node lhs, Node rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }
    public String toString() {
        return "(" + lhs.toString() + name + rhs.toString() + ")";
    }
}

class Plus extends BinOp {
    Plus(Node lhs, Node rhs) {
        super(lhs, rhs);
        name = "+";
    }
    public BigDecimal eval() throws Exception {
        return lhs.eval().add(rhs.eval());
    }
}

class Minus extends BinOp {
    Minus(Node lhs, Node rhs) {
        super(lhs, rhs);
        name = "-";
    }
    public BigDecimal eval() throws Exception {
        return lhs.eval().subtract(rhs.eval());
    }
}

class Multiply extends BinOp {
    Multiply(Node lhs, Node rhs) throws Exception {
        super(lhs, rhs);
        name = "*";
        if (lhs instanceof Null) {
            throw new Exception("Invalid input.");
        }
    }
    public BigDecimal eval() throws Exception {
        return lhs.eval().multiply(rhs.eval());
    }
}

class Divide extends BinOp {
    Divide(Node lhs, Node rhs) throws Exception {
        super(lhs, rhs);
        name = "/";
        if (lhs instanceof Null) {
            throw new Exception("Invalid input.");
        }
    }
    public BigDecimal eval() throws Exception {
        return lhs.eval().divide(rhs.eval());
    }
}

class Exp extends BinOp {
    Exp(Node lhs, Node rhs) throws Exception {
        super(lhs, rhs);
        name = "e";
        if (lhs instanceof Null) {
            throw new Exception("Invalid input.");
        }
    }
    public BigDecimal eval() throws Exception {
        if (rhs instanceof Null) {
            return lhs.eval().multiply(BigDecimal.valueOf(Math.E));
        }
        else {
            BigDecimal raw = rhs.eval();
            if (raw.stripTrailingZeros().scale() > 0) {
                throw new Exception("Only integers can be exp.");
            }
            return lhs.eval().multiply(BigDecimal.valueOf(Math.pow(10.0, raw.doubleValue())));
        }
    }
}

class Power extends BinOp {
    Power(Node lhs, Node rhs) {
        super(lhs, rhs);
        name = "^";
    }
    public BigDecimal eval() throws Exception {
        return BigDecimal.valueOf(Math.pow(lhs.eval().doubleValue(), rhs.eval().doubleValue()));
    }
}

abstract class Function extends Node {
    protected Node val;
    Function(Node val, String name) throws Exception {
        if (val instanceof Null) {
            throw new Exception("Value of function cannot be empty.");
        }
        this.val = val;
        this.name = name;
    }
    public String toString() {
        return name + "(" + val.toString() + ")";
    }
}

class Sin extends Function {
    Sin(Node val) throws Exception {
        super(val, "sin");
    }
    public BigDecimal eval() throws Exception {
        return BigDecimal.valueOf(Math.sin(val.eval().doubleValue()));
    }
}

class Cos extends Function {
    Cos(Node val) throws Exception {
        super(val, "cos");
    }
    public BigDecimal eval() throws Exception {
        return BigDecimal.valueOf(Math.cos(val.eval().doubleValue()));
    }
}

class Tan extends Function {
    Tan(Node val) throws Exception {
        super(val, "tan");
    }
    public BigDecimal eval() throws Exception {
        return BigDecimal.valueOf(Math.tan(val.eval().doubleValue()));
    }
}

class Arcsin extends Function {
    Arcsin(Node val) throws Exception {
        super(val, "arcsin");
    }
    public BigDecimal eval() throws Exception {
        return BigDecimal.valueOf(Math.asin(val.eval().doubleValue()));
    }
}

class Arccos extends Function {
    Arccos(Node val) throws Exception {
        super(val, "arccos");
    }
    public BigDecimal eval() throws Exception {
        return BigDecimal.valueOf(Math.acos(val.eval().doubleValue()));
    }
}

class Arctan extends Function {
    Arctan(Node val) throws Exception {
        super(val, "arctan");
    }
    public BigDecimal eval() throws Exception {
        return BigDecimal.valueOf(Math.atan(val.eval().doubleValue()));
    }
}

class Sqrt extends Function {
    Sqrt(Node val) throws Exception {
        super(val, "sqrt");
    }
    public BigDecimal eval() throws Exception {
        BigDecimal raw = val.eval();
        if (raw.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("Negtive number cannot be sqrt.");
        }
        return BigDecimal.valueOf(Math.sqrt(val.eval().doubleValue()));
    }
}

class Lg extends Function {
    Lg(Node val) throws Exception {
        super(val, "lg");
    }
    public BigDecimal eval() throws Exception {
        return BigDecimal.valueOf(Math.log10(val.eval().doubleValue()));
    }
}

class Ln extends Function {
    Ln(Node val) throws Exception {
        super(val, "ln");
    }
    public BigDecimal eval() throws Exception {
        return BigDecimal.valueOf(Math.log(val.eval().doubleValue()));
    }
}

class Factorial extends Function {
    Factorial(Node val) throws Exception {
        super(val, "!");
    }
    public String toString() {
        return "(" + val.toString() + ")!";
    }
    public BigDecimal eval() throws Exception {
        BigDecimal raw = val.eval();
        if (raw.stripTrailingZeros().scale() > 0) {
            throw new Exception("Only integers can be factorized.");
        }
        long value = raw.longValue();
        BigDecimal res = BigDecimal.ONE;
        for (long i=1; i<=value; i++) {
            res = res.multiply(BigDecimal.valueOf(i));
        }
        return res;
    }
}
