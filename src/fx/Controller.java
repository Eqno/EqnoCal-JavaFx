package fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.lang.String;
import java.math.MathContext;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class Controller implements Initializable {
    final int MAXLEN = 20;

    @FXML public Label mainScreen;
    @FXML public Label secondScreen;

    BigDecimal num = BigDecimal.ZERO;
    public void clear(ActionEvent event) {
        mainScreen.setText("");
    }
    private void appendNum(int dig) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            mainScreen.setText(mainText+new Integer(dig).toString());
            num = num.multiply(BigDecimal.TEN).add(BigDecimal.valueOf(dig));
        }
    }
    private void appendSymbol(char sym) {

    }
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
    public void plus() {
        appendSymbol();
    }
    public void minus() {

    }
    public void multiple() {

    }
    public void divide() {

    }
    public void equal(ActionEvent event) {
        String mainText = mainScreen.getText();
        secondScreen.setText(mainText);
        BigDecimal left = new BigDecimal(mainText);
        mainScreen.setText(left.toString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
