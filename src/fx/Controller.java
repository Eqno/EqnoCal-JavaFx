package fx;

import com.sun.glass.ui.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import java.lang.String;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.net.URL;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    final int MAXLEN = 30;

    @FXML public Label mainScreen;
    @FXML public Label secondScreen;

    public void typeNumZero(ActionEvent event) {
        appendNum(0);
    }
    public void typeNumOne(ActionEvent event) {
        appendNum(1);
    }
    public void typeNumTwo(ActionEvent event) {
        appendNum(2);
    }
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

    boolean start = false;
    boolean lastIsSymbol = false;
    boolean haveOnePoint = false;
    private void appendNum(int dig) {
        lastIsSymbol = false;
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            String newDig = Integer.toString(dig);
            if (mainText.equals("0") || start) {
                mainScreen.setText(newDig);
                start = false;
            }
            else {
                mainScreen.setText(mainText+newDig);
            }
            setFontSize(mainText.length());
        }
    }
    private void appendPoint() {
        lastIsSymbol = false;
        if (start) {
            mainScreen.setText("0.");
        }
        else if (haveOnePoint) {

        }
        else {
            String mainText = mainScreen.getText();
            if (mainText.length() < MAXLEN) {
                mainScreen.setText(mainText+".");
            }
            haveOnePoint = true;
        }
    }
    private void appendSymbol(char sym) {
        if (lastIsSymbol) {
            String mainText = mainScreen.getText();
            if (mainText.length() > 0) {
                mainText = mainText.substring(0, mainText.length()-1);
                mainText += sym;
                mainScreen.setText(mainText);
            }
        }
        else {
            String mainText = mainScreen.getText();
            if (mainText.charAt(0)=='=') {
                mainText = mainText.substring(1, mainText.length());
            }
            if (mainText.length() < MAXLEN) {
                mainScreen.setText(mainText+sym);
            }
            setFontSize(mainText.length());
            haveOnePoint = false;
            lastIsSymbol = true;
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
        String newText = "0";
        if (mainText.length() > 1) {
            newText = mainText.substring(0, mainText.length() - 1);
        }
        lastIsSymbol = judgeSym(newText.charAt(newText.length()-1));
        haveOnePoint = false;
        for (int i=newText.length()-1; i>=0; i--) {
            if (judgeSym(newText.charAt(i))) {
                break;
            }
            if (newText.charAt(i)=='.') {
                haveOnePoint = true;
            }
        }
        mainScreen.setText(newText);
    }
    public void clear(ActionEvent event) {
        mainScreen.setText("0");
        secondScreen.setText("");
        lastIsSymbol = false;
        haveOnePoint = false;
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
        secondScreen.setText(expression);
        String ans = caculate(expression);
        if (ans.equals("NaN")||ans.equals("Infinity")) {
            ans = "不能除以0";
        }
        else {
            ans = "=" + ans;
        }
        mainScreen.setText(ans);
        setFontSize(ans.length());
        lastIsSymbol = false;
        haveOnePoint = false;
        start = true;
    }
    private String caculate(String expression) throws ScriptException {
        StringBuilder evalStringBuilder = new StringBuilder();
        for (int i=0; i<expression.length(); i++) {
            Character c = expression.charAt(i);
            if (c.equals('.') && i>0 && !Character.isDigit(expression.charAt(i-1))) {
                evalStringBuilder.append("0");
            }
            evalStringBuilder.append(c);
            if (c.equals('.') && i<expression.length()-1 && !Character.isDigit(expression.charAt(i+1))) {
                evalStringBuilder.append("0");
            }
            if (c.equals('.') && i==expression.length()-1) {
                evalStringBuilder.append("0");
            }
        }
        String evalString = evalStringBuilder.toString();
        System.out.println(evalString);
        return new ScriptEngineManager().getEngineByName("js").eval(evalString).toString();
    }

    private boolean judgeSym(Character c) {
        return c.equals('+')||c.equals('-')||c.equals('*')||c.equals('/');
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
