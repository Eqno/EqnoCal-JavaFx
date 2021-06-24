package fx;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javax.script.ScriptEngineManager;
import java.io.*;

public class KeyboardController {
    String evalSystem = "ST";

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
    public void percent(ActionEvent event) throws Exception {
        String mainText = mainScreen.getText();
        if (mainText.charAt(0)=='=') {
            mainText = mainText.substring(1);
        }
        if (judgeSym(mainText.charAt(mainText.length()-1))) {
            mainText = mainText.substring(0, mainText.length()-1);
        }
        if (mainText.charAt(mainText.length()-1)==')') {
            setMainScreen(calculate(completeParen(mainText)+"/100"));
            haveOneE = false;
            return ;
        }
        else {
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
    }

    boolean start = false;
    boolean startex = false;
    boolean lastIsSymbol = false;
    boolean haveOnePoint = false;
    private void appendNum(int dig) {
        lastIsSymbol = false;
        String mainText = mainScreen.getText();
        if (mainText.length()<MAXLEN || start || startex) {
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
                if (judgeSym(sym)) {
                    mainText = mainText.substring(0, mainText.length()-1);
                    mainText += sym;
                    setMainScreen(mainText);
                    lastIsSymbol = true;
                }
                else {
                    if (haveOnePoint) {

                    }
                    else {
                        char lastChar = mainText.charAt(mainText.length()-2);
                        if (Character.isDigit(lastChar) || lastChar=='.') {
                            mainText = mainText.substring(0, mainText.length()-1);
                            mainText += sym;
                            setMainScreen(mainText);
                            lastIsSymbol = false;
                        }
                    }

                }
            }
        }
        else {
            String mainText = mainScreen.getText();
            if (startex) {
                setMainScreen("0"+sym);
                startex = false;
            }
            else {
                if (judgeSym(sym)) {
                    if (mainText.charAt(0)=='=') {
                        mainText = mainText.substring(1);
                    }
                    if (mainText.length() < MAXLEN) {
                        if (mainText.length()>0 && mainText.charAt(mainText.length()-1)=='(' && sym!='-') {
                            return ;
                        }
                        else {
                            setMainScreen(mainText+sym);
                            lastIsSymbol = true;
                            haveOnePoint = false;
                            haveOneE = false;
                            eLeftIsSym = false;
                            start = false;
                        }
                    }
                }
                else {
                    if (haveOnePoint) {

                    }
                    else {
                        char lastChar = mainText.charAt(mainText.length()-1);
                        if (Character.isDigit(lastChar) || lastChar=='.') {
                            if (mainText.charAt(0)=='=') {
                                mainText = mainText.substring(1);
                            }
                            if (mainText.length() < MAXLEN) {
                                if (mainText.length()>0 && mainText.charAt(mainText.length()-1)=='(') {
                                    return ;
                                }
                                else {
                                    setMainScreen(mainText+sym);
                                    lastIsSymbol = false;
                                    haveOnePoint = false;
                                    haveOneE = false;
                                    eLeftIsSym = false;
                                    start = false;
                                }
                            }
                        }
                    }
                }
            }
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
            if (mainText.length()>2) {
                boolean flag = false;
                if (mainText.length()>3){
                    char i=mainText.charAt(mainText.length()-1);
                    char j=mainText.charAt(mainText.length()-2);
                    char k=mainText.charAt(mainText.length()-3);
                    char l=mainText.charAt(mainText.length()-4);
                    if ((i=='('&&j=='n'&&k=='i'&&l=='s')
                            ||(i=='('&&j=='s'&&k=='o'&&l=='c')
                            ||(i=='('&&j=='n'&&k=='a'&&l=='t')) {
                        newText = mainText.substring(0, mainText.length() - 4);
                        if (newText.equals("")) {
                            newText = "0";
                        }
                        flag = true;
                    }
                }
                if (mainText.length()>2) {
                    char i=mainText.charAt(mainText.length()-1);
                    char j=mainText.charAt(mainText.length()-2);
                    char k=mainText.charAt(mainText.length()-3);
                    if ((i=='('&&j=='g'&&k=='l')
                            ||(i=='('&&j=='n'&&k=='l')) {
                        newText = mainText.substring(0, mainText.length() - 3);
                        if (newText.equals("")) {
                            newText = "0";
                        }
                        flag = true;
                    }
                }
                if (!flag) {
                    newText = mainText.substring(0, mainText.length() - 1);
                }
            }
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
    public void equal(ActionEvent event) throws Exception {
        String expression = mainScreen.getText();

        if (expression.charAt(0)=='=') {
            expression = expression.substring(1, expression.length());
        }
        Character last = expression.charAt(expression.length()-1);
        if (judgeSym(last)) {
            expression = expression.substring(0, expression.length()-1);
        }

        setSecondScreen(expression);

        // sin
        expression = completeSin(expression);
        // sin

        // sqrt
        expression = completeSqrt(expression);
        // sqrt

        // paren
        expression = completeParen(expression);
        // paren

        String ans = expression;
        if (expression.equals("不能除以0！")) {

        }
        else {
            ans = calculate(expression);
            if (ans.equals("NaN")||ans.equals("Infinity")||ans.equals("-Infinity")) {
                ans = "不能除以0！";
                startex = true;
            }
            if (ans.equals("Error")) {
                ans = "出错！";
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
        if (mainText.length() < MAXLEN || start || startex) {
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
                    if (judgeSym(c)) {
                        eLeftIsSym = true;
                    }
                    if (evalSystem.equals("JS") && c=='π') {
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
        if (mainText.length() < MAXLEN || start || startex) {
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
    public void reciprocal(ActionEvent e) throws Exception {
        String mainText = mainScreen.getText();
        if (mainText.charAt(0)=='=') {
            mainText = mainText.substring(1);
        }
        if (judgeSym(mainText.charAt(mainText.length()-1))) {
            mainText = mainText.substring(0, mainText.length()-1);
        }
        if (mainText.charAt(mainText.length()-1)==')') {
            if (evalSystem.equals("JS")) {
                setMainScreen("暂不支持!");
                lastIsSymbol = false;
                haveOnePoint = false;
                eLeftIsSym = false;
                haveOneE = false;
                start = true;
                numOfLeftParen = 0;
                numOfRightParen = 0;
            }
            if (evalSystem.equals("ST")) {
                makeReciprocal(mainText);
                return ;
            }
        }
        for (int i=mainText.length()-1; i>=0; i--) {
            char c = mainText.charAt(i);
            if (judgeSym(c)) {
                String left = mainText.substring(0, i);
                String right = mainText.substring(i+1);

                // paren
                right = completeParen(right);
                // paren

                if (evalSystem.equals("JS")) {
                    right = calculate("Math#pow("+right+",-1)");
                }
                if (evalSystem.equals("ST")) {
                    right = calculate(right+"^(-1)");
                }

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
                else if (right.equals("Error")) {
                    setMainScreen("出错！");
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
            makeReciprocal(mainText);
            return ;
        }
    }
    private void makeReciprocal(String ans) throws Exception {
        // paren
        ans = completeParen(ans);
        // paren

        if (evalSystem.equals("JS")) {
            ans = calculate("Math#pow("+ans+",-1)");
        }
        if (evalSystem.equals("ST")) {
            ans = calculate(ans+"^(-1)");
        }


        if (ans.equals("Infinity")) {
            ans = "0没有倒数！";
            startex = true;
        }
        else if (ans.equals("Error")) {
            ans = "出错！";
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
    public void factorial(ActionEvent e) {
        if (evalSystem.equals("ST")) {
            appendSymbol('!');
        }
        if (evalSystem.equals("JS")) {
            setMainScreen("暂不支持!");
            lastIsSymbol = false;
            haveOnePoint = false;
            eLeftIsSym = false;
            haveOneE = false;
            start = true;
            numOfLeftParen = 0;
            numOfRightParen = 0;
        }
    }
    public void squareRoot(ActionEvent e) {
        String mainText = mainScreen.getText();
        if (mainText.length()<MAXLEN || start || startex) {
            if (mainText.equals("0") || start ||startex) {
                setMainScreen("√");
                start = false;
                startex = false;
            }
            else if (mainText.charAt(mainText.length()-1)=='.' || mainText.charAt(mainText.length()-1)=='^') {

            }
            else {
                setMainScreen(mainText+"√");
                haveOneE = false;
                lastIsSymbol = false;
                haveOnePoint = false;
                eLeftIsSym = false;
                haveOneE = false;
                start = false;
                startex = false;
            }
        }
    }
    public void power(ActionEvent e) {
        if (evalSystem.equals("ST")) {
            String mainText = mainScreen.getText();
            if (mainText.length()<MAXLEN || start || startex) {
                if (mainText.equals("0") || start ||startex) {
                    setMainScreen("^");
                    start = false;
                    startex = false;
                }
                else {
                    char lastChar = mainText.charAt(mainText.length()-1);
                    if (lastIsSymbol || lastChar=='√' || lastChar=='!' || lastChar=='(') {

                    }
                    else {
                        setMainScreen(mainText+"^");
                        haveOneE = false;
                        haveOnePoint = false;
                        eLeftIsSym = false;
                        haveOneE = false;

                        lastIsSymbol = true;
                    }
                }
            }
        }
        if (evalSystem.equals("JS")) {
            setMainScreen("暂不支持!");
            lastIsSymbol = false;
            haveOnePoint = false;
            eLeftIsSym = false;
            haveOneE = false;
            start = true;
            numOfLeftParen = 0;
            numOfRightParen = 0;
        }
    }
    public void lg(ActionEvent e) {
        appendSin("lg(");
    }
    public void ln(ActionEvent e) {
        appendSin("ln(");
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
                haveOnePoint = false;
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
                        || mainText.charAt(mainText.length()-1)=='(')
                        || mainText.charAt(mainText.length()-1)=='√') {

                }
                else {
                    if (numOfRightParen < numOfLeftParen) {
                        lastIsSymbol = false;
                        setMainScreen(mainText+")");
                        haveOnePoint = false;
                        haveOneE = false;
                        numOfRightParen ++;
                    }
                }
            }
        }
    }
    public void sin(ActionEvent e) {
        appendSin("sin(");
    }
    public void cos(ActionEvent e) {
        appendSin("cos(");
    }
    public void tan(ActionEvent e) {
        appendSin("tan(");
    }
    public void secondPage(ActionEvent e) {
        setMainScreen("敬请期待！");
        lastIsSymbol = false;
        haveOnePoint = false;
        eLeftIsSym = false;
        haveOneE = false;
        start = true;
        numOfLeftParen = 0;
        numOfRightParen = 0;
    }
    public void openFile(ActionEvent e) throws Exception {
        File file = new File("expression.txt");
        BufferedReader bufr;
        try {
            bufr = new BufferedReader(new FileReader(file));
        }
        catch (Exception ee) {
            file.createNewFile();
            bufr = new BufferedReader(new FileReader(file));
        }
        String mainText = mainScreen.getText();
        if (mainText.equals("0")) {
            String input = bufr.readLine();
            bufr.close();
            if (input==null) {
                setSecondScreen(mainScreen.getText());
                setMainScreen("输入为空！");
            }
            else {
                setSecondScreen(input);
                String ans = calculate(input);
                setMainScreen(ans);
                BufferedWriter bufw = new BufferedWriter(new FileWriter(file, false));
                bufw.write(ans);
                bufw.close();
            }
        }
        else {
            if (mainText.charAt(0)=='=') {
                mainText = mainText.substring(1);
            }
            setSecondScreen(mainText);
            String ans = calculate(mainText);
            setMainScreen(ans);
            BufferedWriter bufw = new BufferedWriter(new FileWriter(file, false));
            bufw.write(ans);
            bufw.close();
        }
        lastIsSymbol = false;
        haveOnePoint = false;
        eLeftIsSym = false;
        haveOneE = false;
        start = true;
        numOfLeftParen = 0;
        numOfRightParen = 0;
    }
    private void appendSin(String s) {
        String mainText = mainScreen.getText();
        if (mainText.charAt(mainText.length()-1)=='.') {

        }
        else if (mainText.length()<MAXLEN || start || startex) {
            if (mainText.equals("0") || start || startex) {
                setMainScreen(s);
                numOfLeftParen ++;
                start = false;
                startex = false;
            }
            else {
                setMainScreen(mainText+s);
                numOfLeftParen ++;
                haveOneE = false;
                lastIsSymbol = false;
                haveOnePoint = false;
                eLeftIsSym = false;
            }
        }
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
    private boolean judgeMul(Character c) {
        if (judgeDigit(c)) {
            return true;
        }
        if (c.equals(')')) {
            return true;
        }
        return false;
    }
    private String completeSqrt(String expression) {
        String ans = "";
        boolean waitForRight = false;
        boolean skip = false;
        for (int i=0; i<expression.length(); i++) {
            char thisChar = expression.charAt(i);
            if (waitForRight) {
                if (skip) {
                    skip = false;
                }
                else if (judgeSym(thisChar)||thisChar=='π'||thisChar=='e') {
                        ans += ")";
                        waitForRight = false;
                }
            }
            if (thisChar == '√') {
                if (i < expression.length() - 1) {
                    if (i>0 && judgeMul(expression.charAt(i-1))) {
                        ans += "*";
                    }
                    if (expression.charAt(i+1) == '(') {
                        if (evalSystem.equals("JS")) {
                            ans += "Math#sqrt";
                        }
                        if (evalSystem.equals("ST")) {
                            ans += "sqrt";
                        }
                    }
                    else {
                        if (evalSystem.equals("JS")) {
                            ans += "Math#sqrt(";
                        }
                        else {
                            ans += "sqrt(";
                        }
                        waitForRight = true;
                        skip = true;
                    }
                }
                else {
                    if (i>0 && judgeMul(expression.charAt(i-1))) {
                        ans += "*";
                    }
                    if (evalSystem.equals("JS")) {
                        ans += "Math#sqrt(";
                    }
                    else {
                        ans += "sqrt(";
                    }
                }
            }
            else  {
                ans += thisChar;
            }
        }
        return ans;
    }
    private  String completeSin(String expression) {
        String ans = "";
        for (int i=0; i<expression.length(); i++) {
            char thisChar = expression.charAt(i);
            if (i<expression.length()-2) {
                boolean flag = false;
                if (i<expression.length()-3) {
                    // sin, cot, tan
                    if ((thisChar=='s'&&expression.charAt(i+1)=='i'&&expression.charAt(i+2)=='n')
                            || (thisChar=='c'&&expression.charAt(i+1)=='o'&&expression.charAt(i+2)=='s')
                            || (thisChar=='t'&&expression.charAt(i+1)=='a'&&expression.charAt(i+2)=='n')){
                        if (i>0 && judgeMul(expression.charAt(i-1))) {
                            ans += "*";
                        }
                        if (evalSystem.equals("JS")) {
                            ans = ans + "Math#" + thisChar;
                        }
                        if (evalSystem.equals("ST")) {
                            ans = ans + thisChar;
                        }
                        flag = true;
                    }
                }
                if (i<expression.length()-2) {
                    if (thisChar=='l'&&expression.charAt(i+1)=='g') {
                        if (i>0 && judgeMul(expression.charAt(i-1))) {
                            ans += "*";
                        }
                        if (evalSystem.equals("JS")) {
                            ans = ans + "Math#logXY";
                            i ++;
                        }
                        if (evalSystem.equals("ST")) {
                            ans = ans + "lg";
                            i ++;
                        }
                        flag = true;
                    }
                    else if (thisChar=='l'&&expression.charAt(i+1)=='n') {
                        if (i>0 && judgeMul(expression.charAt(i-1))) {
                            ans += "*";
                        }
                        if (evalSystem.equals("JS")) {
                            ans = ans + "Math#log";

                            i ++;
                        }
                        if (evalSystem.equals("ST")) {
                            ans = ans + "ln";
                            i ++;
                        }
                        flag = true;
                    }
                }
                if (!flag) {
                    ans += thisChar;
                }
            }
            else {
                ans += thisChar;
            }
        }
        return ans;
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
    private String calculate(String expression) throws Exception {
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
                if (i>0 && Character.isDigit(expression.charAt(i-1)) && i<expression.length()-1 && Character.isDigit(expression.charAt(i+1))) {
                    evalStringBuilder.append("e");
                }
                else if (i>0 && judgeDigit(expression.charAt(i-1)) && i<expression.length()-1 && (expression.charAt(i+1)=='+'||expression.charAt(i+1)=='-')) {
                    evalStringBuilder.append("e");
                }
                else {
                    if (i>0 && judgeDigit(expression.charAt(i-1))) {
                        evalStringBuilder.append("*");
                    }
                    if (evalSystem.equals("JS")) {
                        evalStringBuilder.append("Math.E");
                    }
                    if (evalSystem.equals("ST")) {
                        evalStringBuilder.append("E");
                    }
                    if (i<expression.length()-1 && judgeDigit(expression.charAt(i+1))) {
                        evalStringBuilder.append("*");
                    }
                }
            }
                /*if (i>0 && (judgeDigit(expression.charAt(i-1))
                        ||(i>1 && expression.charAt(i-1)=='(' && expression.charAt(i-2)=='t'))) {
                    if (i<expression.length()-1 && Character.isDigit(expression.charAt(i+1))) {
                        if (expression.charAt(i-1)=='π') {
                            if (evalSystem.equals("JS")) {
                                evalStringBuilder.append("*Math.E");
                            }
                            else {
                                evalStringBuilder.append("e");
                            }
                        }
                        else if (i>1 && expression.charAt(i-1)=='(' && expression.charAt(i-2)=='t') {
                            if (evalSystem.equals("JS")) {
                                evalStringBuilder.append("Math.E*");
                            }
                            else {
                                evalStringBuilder.append("E");
                            }
                        }
                        else {
                                evalStringBuilder.append("e");
                        }
                    }
                    else if (i<expression.length()-1 && (expression.charAt(i+1)=='-'||expression.charAt(i+1)=='+')
                        && i<expression.length()-2 && Character.isDigit(expression.charAt(i+2))) {

                        if (evalSystem.equals("JS")) {
                            evalStringBuilder.append("*Math.E");
                        }
                        else {
                            evalStringBuilder.append("e");
                        }
                    }
                    else {
                        if (i>1 && expression.charAt(i-1)=='(' && expression.charAt(i-2)=='t') {

                            if (evalSystem.equals("JS")) {
                                evalStringBuilder.append("Math.E");
                            }
                            if (evalSystem.equals("ST")) {
                                evalStringBuilder.append("E");
                            }
                        }
                        else {

                            if (evalSystem.equals("JS")) {
                                evalStringBuilder.append("*Math.E");
                            }
                            if (evalSystem.equals("ST")) {
                                evalStringBuilder.append("*E");
                            }
                        }
                    }
                }
                else {

                    if (evalSystem.equals("JS")) {
                        evalStringBuilder.append("Math.E");
                    }
                    if (evalSystem.equals("ST")) {
                        evalStringBuilder.append("E");
                    }
                    if (i<expression.length()-1 && Character.isDigit(expression.charAt(i+1))) {
                        evalStringBuilder.append("*");
                    }
                }
            }*/
            // e

            // π
            else if (c.equals('π')) {
                if (i>0 && judgeDigit(expression.charAt(i-1))) {
                    if (expression.charAt(i-1)=='e') {

                    }
                    else {
                        evalStringBuilder.append("*");
                    }
                }
                if (evalSystem.equals("JS")) {
                    evalStringBuilder.append("Math.PI");
                }
                if (evalSystem.equals("ST")) {
                    evalStringBuilder.append("π");
                }
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
                if (i>0 && (judgeDigit(expression.charAt(i-1))
                        || expression.charAt(i-1)=='.')) {
                    evalStringBuilder.append("*");
                }
                evalStringBuilder.append(c);
            }
            else if (c.equals(')')) {
                evalStringBuilder.append(c);
                if (i<expression.length()-1
                        && (judgeDigit(expression.charAt(i+1))
                        || expression.charAt(i+1)=='(' || expression.charAt(i+1)=='.')) {
                    evalStringBuilder.append("*");
                }
            }
            // paren

            // #
            else if (c.equals('#')) {
                evalStringBuilder.append(".");
            }
            else if (c.equals('X')) {
                evalStringBuilder.append("1");
            }
            else if (c.equals('Y')) {
                evalStringBuilder.append("0");
            }
            // #

            else {
                evalStringBuilder.append(c);
            }
        }
        String evalString = evalStringBuilder.toString();
        System.out.println(evalString);
        if (evalSystem.equals("JS")) {
            try {
                return new ScriptEngineManager().getEngineByName("js").eval(evalString).toString();
            }
            catch (Exception e) {
                return "Error";
            }
        }
        if (evalSystem.equals("ST")) {
            try {
                return (""+new Calculator().eval(evalString).doubleValue());
            }
            catch (Exception e) {
                return "Error";
            }
        }
        return "No evalSystem!";
    }
}
