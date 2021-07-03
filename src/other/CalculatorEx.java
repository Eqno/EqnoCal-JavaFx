/*
package other;

import java.math.BigDecimal;

public class CalculatorEx {
    public static void main(String[] args) throws Exception {
        String expression = "1+1";
        System.out.println(expression+"="+eval(expression));
    }
    public static String eval(String expression) throws Exception {
        return ""+SyntaxTree.parse(expression).eval().doubleValue();
    }
}

// Node
class Node {
    protected String name;
    protected BigDecimal val;
    protected Node(String name, BigDecimal val) {
        this.name = name;
        this.val = val;
    }
    public String toString() { return name; }
    public BigDecimal eval() { return val; }
}
// Node

// type
class Null extends Node {
    public Null() { super("", BigDecimal.ZERO); }
}
class Number extends Node {
    protected Number(String num) { super(num, new BigDecimal(num)); }
    protected Number(BigDecimal num) { super(num.toString(), num);}
    protected Number(String name, BigDecimal num) { super(name, num); }
}
// type

// num
class PI extends Number {
    protected PI() { super("π", new BigDecimal(Math.PI)); }
}
class E extends Number {
    protected E() { super("e", new BigDecimal(Math.E)); }
}
// num




class SyntaxTree {
    final static String PI = "π";
    final static String E = "e";
    final static String EXP = "E";
    final static boolean debug = false;

    protected Node root;

    public SyntaxTree(Node root) {
        this.root = root;
    }
}*/
