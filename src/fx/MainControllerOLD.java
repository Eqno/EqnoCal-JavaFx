package fx;

public class MainControllerOLD {
    /*String numString = "";
    private void appendNum(int dig) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            String newDig = new Integer(dig).toString();
            if (mainText.equals("0")) {
                mainScreen.setText(newDig);
            }
            else {
                mainScreen.setText(mainText+newDig);
            }
            numString += newDig;
        }
    }
    BigDecimal ans = BigDecimal.ZERO;
    BigDecimal res = BigDecimal.ONE;
    public void clear(ActionEvent event) {
        mainScreen.setText("0");
        secondScreen.setText("");
        res = BigDecimal.ONE;
        ans = BigDecimal.ZERO;
        numString = "";
        wait = false;
    }
    char lastPlus = '+', lastMul = '*', lastSym = '+';
    boolean wait = false;
    private void appendSymbol(char sym) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            mainScreen.setText(mainText+new Character(sym).toString());
            BigDecimal num;
            if (numString.length() > 0) {
                num = new BigDecimal(numString);
                numString = "";
            }
            else {
                num = new BigDecimal("0");
            }
            if (sym == '+' || sym == '-') {
                if (wait) {
                    calculate(lastMul, num);
                    calculate(lastPlus, res);
                    res = BigDecimal.ONE;
                    lastPlus = sym;
                    wait = false;
                }
                else {
                    calculate(lastPlus, num);
                    lastPlus = sym;
                }
            }
            if (sym == '*' || sym == '/') {
                if (wait) {
                    calculate(lastMul, num);
                    lastMul = sym;
                }
                else {
                    calculate(lastMul, num);
                    lastMul = sym;
                    wait = true;
                }
            }
            lastSym = sym;
        }
    }
    private void calculate(char sym, BigDecimal tmp) {
        if (sym == '+') {
            ans = ans.add(tmp);
        }
        if (sym == '-') {
            ans = ans.subtract(tmp);
        }
        if (sym == '*') {
            res = res.multiply(tmp);
        }
import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;
import java.util.Stack;

public class Controller implements Initializable {
    final int MAXLEN = 20;

    @FXML public Label mainScreen;
    @FXML public Label secondScreen;

    /*String numString = "";
    private void appendNum(int dig) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            String newDig = new Integer(dig).toString();
            if (mainText.equals("0")) {
                mainScreen.setText(newDig);
            }
            else {
                mainScreen.setText(mainText+newDig);
            }
            numString += newDig;
        }
    }
    BigDecimal ans = BigDecimal.ZERO;
    BigDecimal res = BigDecimal.ONE;
    public void clear(ActionEvent event) {
        mainScreen.setText("0");
        secondScreen.setText("");
        res = BigDecimal.ONE;
        ans = BigDecimal.ZERO;
        numString = "";
        wait = false;
    }
    char lastPlus = '+', lastMul = '*', lastSym = '+';
    boolean wait = false;
    private void appendSymbol(char sym) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            mainScreen.setText(mainText+new Character(sym).toString());
            BigDecimal num;
            if (numString.length() > 0) {
                num = new BigDecimal(numString);
                numString = "";
            }
            else {
                num = new BigDecimal("0");
            }
            if (sym == '+' || sym == '-') {
                if (wait) {
                    calculate(lastMul, num);
                    calculate(lastPlus, res);
                    res = BigDecimal.ONE;
                    lastPlus = sym;
                    wait = false;
                }
                else {
                    calculate(lastPlus, num);
                    lastPlus = sym;
                }
            }
            if (sym == '*' || sym == '/') {
                if (wait) {
                    calculate(lastMul, num);
                    lastMul = sym;
                }
                else {
                    calculate(lastMul, num);
                    lastMul = sym;
                    wait = true;
                }
            }
            lastSym = sym;
        }
    }
    private void calculate(char sym, BigDecimal tmp) {
        if (sym == '+') {
            ans = ans.add(tmp);
        }
        if (sym == '-') {
            ans = ans.subtract(tmp);
        }
        if (sym == '*') {
            res = res.multiply(tmp);
        }
        if (sym == '/') {
            res = res.divide(tmp);
        }
    }
    public void equal(ActionEvent event) {
        BigDecimal num;
        if (numString.length() > 0) {
            num = new BigDecimal(numString);
            numString = "";
        }
        else {
            num = new BigDecimal("0");
        }
        if (lastSym == '+' || lastSym == '-') {
            calculate(lastPlus, num);
        }
        if (lastSym == '*' || lastSym == '/') {
            calculate(lastMul, num);
            calculate(lastPlus, res);
        }
        secondScreen.setText(mainScreen.getText());
        mainScreen.setText(ans.toString());
        res = BigDecimal.ONE;
        lastPlus = '+';
        lastMul = '*';
        lastSym = '+';
        numString = "";
        wait = false;
    }*/
}
