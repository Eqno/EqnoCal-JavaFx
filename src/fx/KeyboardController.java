package fx;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import sun.print.PSPrinterJob;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class KeyboardController {
    final int MAXLEN = 30;
    private Label mainScreen, secondScreen;
    private static String mainScreenText = "0", secondScreenText = "";
    private void setMainScreen(String s) {
        mainScreen.setText(s);
        mainScreenText = s;
        setFontSize(s.length());
    }
    private void setSecondScreen(String s) {
        secondScreen.setText(s);
        secondScreenText = s;
    }

    public void setMainScreen(Label mainScreen) {
        this.mainScreen = mainScreen;
        mainScreen.setText(mainScreenText);
    }
    public void setSecondScreen(Label secondScreen) {
        this.secondScreen = secondScreen;
        secondScreen.setText(secondScreenText);
    }

    public void typeNumZero(ActionEvent event) {
        appendNum(0);
    }
    public void typeNumOne(ActionEvent event) {
        appendNum(1);
    }
    public void typeNumTwo(ActionEvent event) { appendNum(2); }
    public void typeNumThree(ActionEvent event) {
        appendNum(3);
    }
    public void typeNumFour(ActionEvent event) {
        appendNum(4);
    }
    public void typeNumFive(ActionEvent event) {
        appendNum(5);
    }
    public void typeNumSix(ActionEvent event) {
        appendNum(6);
    }
    public void typeNumSeven(ActionEvent event) {
        appendNum(7);
    }
    public void typeNumEight(ActionEvent event) {
        appendNum(8);
    }
    public void typeNumNine(ActionEvent event) {
        appendNum(9);
    }
    public void point(ActionEvent event) {
        appendPoint();
    }
    public void plus(ActionEvent event) {
        appendSymbol('+');
    }
    public void minus(ActionEvent event) {
        appendSymbol('-');
    }
    public void multiple(ActionEvent event) {
        appendSymbol('*');
    }
    public void divide(ActionEvent event) {
        appendSymbol('/');
    }
    public void percent(ActionEvent event) throws ScriptException {
        String mainText = mainScreen.getText();
        if (mainText.charAt(0)=='=') {
            mainText = mainText.substring(1);
        }
        if (judgeSym(mainText.charAt(mainText.length()-1))) {
            mainText = mainText.substring(0, mainText.length()-1);
        }
        for (int i=mainText.length()-1; i>=0; i--) {
            char c = mainText.charAt(i);
            if (judgeSym(c)) {
                String left = mainText.substring(0, i);
                if (c=='+'||c=='-') {
                    String calLeft = completeParen(left);
                    String calRight = completeParen(mainText.substring(i+1));
                    String right = calculate(calLeft+"/100*"+calRight);
                    for (int j=right.length()-1; j>=0; j--) {
                        if (right.charAt(j)=='0') {

                        }
                        else if (right.charAt(j)=='.') {
                            right = right.substring(0, j);
                        }
                        else {
                            break;
                        }
                    }
                    setMainScreen(left+c+right);
                }
                else {
                    String calRight = completeParen(mainText.substring(i+1));
                    String right = calculate(calRight+"/100");
                    setMainScreen(left+c+right);
                    haveOneE = false;
                }
                return ;
            }
        }
        if (mainText.length() < MAXLEN) {
            setMainScreen(calculate(completeParen(mainText)+"/100"));
            haveOneE = false;
        }
    }

    boolean start = false;
    boolean startex = false;
    boolean lastIsSymbol = false;
    boolean haveOnePoint = false;
    private void appendNum(int dig) {
        lastIsSymbol = false;
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            String newDig = Integer.toString(dig);
            if (mainText.equals("0") || start || startex) {
                setMainScreen(newDig);
                start = false;
                startex = false;
            }
            else {
                if (mainText.length()>0 && mainText.charAt(mainText.length()-1)=='e') {
                    if (eLeftIsSym) {

                    }
                    else {
                        setMainScreen(mainText+newDig);
                    }
                }
                else {
                    setMainScreen(mainText+newDig);
                }
            }
        }
    }
    private void appendPoint() {
        String mainText = mainScreen.getText();
        if (haveOneE || (mainText.length()>0 && mainText.charAt(mainText.length()-1)=='π')) {

        }
        else {
            lastIsSymbol = false;
            if (start) {
                setMainScreen("0.");
                start = false;
            }
            else if (haveOnePoint) {

            }
            else {
                if (mainText.length() < MAXLEN) {
                    setMainScreen(mainText+".");
                }
                haveOnePoint = true;
            }
        }
    }
    private void appendSymbol(char sym) {
        if (lastIsSymbol) {
            String mainText = mainScreen.getText();
            if (mainText.length() > 0) {
                mainText = mainText.substring(0, mainText.length()-1);
                mainText += sym;
                setMainScreen(mainText);
            }
        }
        else {
            String mainText = mainScreen.getText();
            if (startex) {
                setMainScreen("0"+sym);
                startex = false;
            }
            else {
                if (mainText.charAt(0)=='=') {
                    mainText = mainText.substring(1);
                }
                if (mainText.length() < MAXLEN) {
                    if (mainText.length()>0 && mainText.charAt(mainText.length()-1)=='(') {
                        return ;
                    }
                    else {
                        setMainScreen(mainText+sym);
                    }
                }
            }
            haveOnePoint = false;
            haveOneE = false;
            lastIsSymbol = true;
            eLeftIsSym = false;
            start = false;
        }
    }
    private void setFontSize(int len) {
        if (len > 24) {
            mainScreen.setFont(new Font("Microsoft YaHei", 40));
        }
        else if (len > 18) {
            mainScreen.setFont(new Font("Microsoft YaHei", 45));
        }
        else if (len > 12) {
            mainScreen.setFont(new Font("Microsoft YaHei", 50));
        }
        else if (len > 6) {
            mainScreen.setFont(new Font("Microsoft YaHei", 55));
        }
        else {
            mainScreen.setFont(new Font("Microsoft YaHei", 60));
        }
    }

    public void delete(ActionEvent event) {
        String mainText = mainScreen.getText();
        if (mainText.charAt(mainText.length()-1) == '(') {
            numOfLeftParen --;
        }
        if (mainText.charAt(mainText.length()-1) == ')') {
            numOfRightParen --;
        }
        String newText = "0";
        if (mainText.length() > 1) {
            newText = mainText.substring(0, mainText.length() - 1);
        }
        lastIsSymbol = judgeSym(newText.charAt(newText.length()-1));
        haveOnePoint = false;
        eLeftIsSym = false;
        haveOneE = false;

        for (int i=newText.length()-1; i>=0; i--) {
            if (judgeSym(newText.charAt(i))) {
                if (i<newText.length()-1 && newText.charAt(i+1)=='e') {
                    eLeftIsSym = true;
                }
                break;
            }
            if (newText.charAt(i) == '.') {
                haveOnePoint = true;
            }
            if (newText.charAt(i) == 'e') {
                haveOneE = true;
            }
        }
        setMainScreen(newText);
    }
    public void clear(ActionEvent event) {
        setMainScreen("0");
        setSecondScreen("");
        lastIsSymbol = false;
        haveOnePoint = false;
        eLeftIsSym = false;
        haveOneE = false;
        numOfLeftParen = 0;
        numOfRightParen = 0;
    }
    public void equal(ActionEvent event) throws ScriptException {
        String expression = mainScreen.getText();
        if (expression.charAt(0)=='=') {
            expression = expression.substring(1, expression.length());
        }
        Character last = expression.charAt(expression.length()-1);
        if (judgeSym(last)) {
            expression = expression.substring(0, expression.length()-1);
        }

        // paren
        expression = completeParen(expression);
        // paren

        setSecondScreen(expression);
        String ans = expression;
        if (expression.equals("不能除以0！")) {

        }
        else {
            ans = calculate(expression);
            if (ans.equals("NaN")||ans.equals("Infinity")||ans.equals("-Infinity")) {
                ans = "不能除以0！";
                startex = true;
            }
            else {
                ans = "=" + ans;
            }
        }
        setMainScreen(ans);
        lastIsSymbol = false;
        haveOnePoint = false;
        eLeftIsSym = false;
        haveOneE = false;
        start = true;
        numOfLeftParen = 0;
        numOfRightParen = 0;
    }
    private boolean judgeSym(Character c) {
        return c.equals('+')||c.equals('-')||c.equals('*')||c.equals('/');
    }

    boolean haveOneE = false;
    boolean eLeftIsSym = false;
    public void e(ActionEvent e) {
        lastIsSymbol = false;
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            if (mainText.equals("0") || start || startex) {
                setMainScreen("e");
                haveOneE = true;
                eLeftIsSym = true;
                start = false;
                startex = false;
            }
            else {
                if (haveOneE) {

                }
                else if (mainText.length()>0 && mainText.charAt(mainText.length()-1)=='.') {

                }
                else {
                    char c = mainText.charAt(mainText.length()-1);
                    if (judgeSym(c) || c=='π') {
                        eLeftIsSym = true;
                    }
                    setMainScreen(mainText+"e");
                    haveOneE = true;
                }
            }
        }
    }
    public void pi(ActionEvent e) {
        lastIsSymbol = false;
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            if (mainText.equals("0") || start || startex) {
                setMainScreen("π");
                start = false;
                startex = false;
            }
            else {
                if (mainText.length()>0 && mainText.charAt(mainText.length()-1)=='.') {

                }
                else {
                    setMainScreen(mainText+"π");
                }
            }
        }
    }
    public void reciprocal(ActionEvent e) throws ScriptException {
        String mainText = mainScreen.getText();
        if (mainText.charAt(0)=='=') {
            mainText = mainText.substring(1);
        }
        if (judgeSym(mainText.charAt(mainText.length()-1))) {
            mainText = mainText.substring(0, mainText.length()-1);
        }
        for (int i=mainText.length()-1; i>=0; i--) {
            char c = mainText.charAt(i);
            if (judgeSym(c)) {
                String left = mainText.substring(0, i);
                String right = mainText.substring(i+1);

                // paren
                right = completeParen(right);
                // paren

                right = calculate("Math#pow("+right+",-1)");
                if (right.equals("Infinity")) {
                    setMainScreen("0没有倒数！");
                    startex = true;
                    haveOneE = false;
                    lastIsSymbol = false;
                    haveOnePoint = false;
                    eLeftIsSym = false;
                    haveOneE = false;
                    start = true;
                    return ;
                }
                else {
                    setMainScreen(left+c+right);
                    haveOneE = false;
                    return ;
                }
            }
        }
        if (mainText.length() < MAXLEN) {
            String ans = mainText;

            // paren
            ans = completeParen(ans);
            // paren

            ans = calculate("Math#pow("+ans+",-1)");
            if (ans.equals("Infinity")) {
                ans = "0没有倒数！";
                startex = true;
            }
            setMainScreen(ans);
            haveOneE = false;
            lastIsSymbol = false;
            haveOnePoint = false;
            eLeftIsSym = false;
            haveOneE = false;
            start = true;
        }
    }
    public void factorial(ActionEvent e) {

    }
    public void squareRoot(ActionEvent e) {

    }
    public void power(ActionEvent e) {

    }
    public void lg(ActionEvent e) {

    }
    public void ln(ActionEvent e) {

    }
    int numOfLeftParen = 0, numOfRightParen = 0;
    public void leftParen(ActionEvent e) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            lastIsSymbol = false;
            if (mainText.equals("0") || start || startex) {
                setMainScreen("(");
                numOfLeftParen ++;
                start = false;
                startex = false;
            }
            else {
                setMainScreen(mainText+"(");
                haveOneE = false;
                numOfLeftParen ++;
            }
        }
    }
    public void rightParen(ActionEvent e) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            if (mainText.equals("0") || start || startex) {

            }
            else {
                if (mainText.length()>0 &&
                        (judgeSym(mainText.charAt(mainText.length()-1))
                        || mainText.charAt(mainText.length()-1)=='(')) {

                }
                else {
                    if (numOfRightParen < numOfLeftParen) {
                        lastIsSymbol = false;
                        setMainScreen(mainText+")");
                        haveOneE = false;
                        numOfRightParen ++;
                    }
                }
            }
        }
    }
    public void sin(ActionEvent e) {

    }
    public void cos(ActionEvent e) {

    }
    public void tan(ActionEvent e) {

    }
    public void secondPage(ActionEvent e) {

    }
    public void openFile(ActionEvent e) {

    }
    private boolean judgeDigit(Character c) {
        if (Character.isDigit(c)) {
            return true;
        }
        if (c.equals('π')) {
            return true;
        }
        if (c.equals('e')) {
            return true;
        }
        return false;
    }
    private String completeParen(String expression) {
        boolean isExistOther = false;
        int leftParen = 0, rightParen = 0;
        for (int i=0; i<expression.length(); i++) {
            if (expression.charAt(i)=='(') {
                leftParen ++;
            }
            else if (expression.charAt(i)==')') {
                rightParen ++;
            }
            else {
                isExistOther = true;
            }
        }
        if (expression.charAt(expression.length()-1)=='(') {
            if (isExistOther) {
                expression += "1";
            }
            else {
                expression += "0";
            }
        }
        for (; rightParen<leftParen; rightParen++) {
            expression += ")";
        }
        return expression;
    }
    private String calculate(String expression) throws ScriptException {
        StringBuilder evalStringBuilder = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            Character c = expression.charAt(i);

            // point
            if (c.equals('.')) {
                if (i > 0 && !judgeDigit(expression.charAt(i - 1))) {
                    evalStringBuilder.append("0");
                }
                evalStringBuilder.append(".");
                if (i < expression.length() - 1 && !judgeDigit(expression.charAt(i + 1))) {
                    evalStringBuilder.append("0");
                }
                if (i == expression.length() - 1) {
                    evalStringBuilder.append("0");
                }
            }
            // point

            // e
            else if (c.equals('e')) {
                if (i<expression.length()-1 && judgeDigit(expression.charAt(i+1))) {
                    if (expression.charAt(i+1)=='π') {
                        if (i>0 && judgeDigit(expression.charAt(i-1))) {
                            evalStringBuilder.append("*");
                        }
                        evalStringBuilder.append("Math.E");
                    }
                    else {
                        evalStringBuilder.append(c);
                    }
                }
                else {
                    if (i>0 && judgeDigit(expression.charAt(i-1))) {
                        evalStringBuilder.append("*");
                    }
                    evalStringBuilder.append("Math.E");
                    if (i<expression.length()-1 && judgeDigit(expression.charAt(i+1))) {
                        evalStringBuilder.append("*");
                    }
                }
            }
            // e

            // π
            else if (c.equals('π')) {
                if (i>0 && judgeDigit(expression.charAt(i-1))) {
                    evalStringBuilder.append("*");
                }
                evalStringBuilder.append("Math.PI");
                if (i<expression.length()-1 && judgeDigit(expression.charAt(i+1))) {
                    if (expression.charAt(i+1)=='π'||expression.charAt(i+1)=='e') {

                    }
                    else {
                        evalStringBuilder.append("*");
                    }
                }
            }
            // π

            // paren
            else if (c.equals('(')) {
                if (i>0 && judgeDigit(expression.charAt(i-1))) {
                    evalStringBuilder.append("*");
                }
                evalStringBuilder.append(c);
            }
            else if (c.equals(')')) {
                evalStringBuilder.append(c);
                if (i<expression.length()-1
                        && (judgeDigit(expression.charAt(i+1))
                        ||expression.charAt(i+1)=='(')) {
                    evalStringBuilder.append("*");
                }
            }
            // paren

            else {
                if (c.equals('#')) {
                    c = '.';
                }
                evalStringBuilder.append(c);
            }
        }
        String evalString = evalStringBuilder.toString();
        System.out.println(evalString);
        return new ScriptEngineManager().getEngineByName("js").eval(evalString).toString();
    }
}
