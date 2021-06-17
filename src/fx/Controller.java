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

public class Controller implements Initializable {
    final int MAXLEN = 20;

    @FXML public Label mainScreen;
    @FXML public Label secondScreen;
    public void AC(ActionEvent event) {
        mainScreen.setText("");
    }
    public void typeNumZero(ActionEvent event) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            mainScreen.setText(mainText+"0");
        }
    }
    public void typeNumOne(ActionEvent event) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            mainScreen.setText(mainText+"1");
        }
    }
    public void typeNumTwo(ActionEvent event) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            mainScreen.setText(mainText+"2");
        }
    }
    public void typeNumThree(ActionEvent event) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            mainScreen.setText(mainText+"3");
        }
    }
    public void typeNumFour(ActionEvent event) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            mainScreen.setText(mainText+"4");
        }
    }
    public void typeNumFive(ActionEvent event) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            mainScreen.setText(mainText+"5");
        }
    }
    public void typeNumSix(ActionEvent event) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            mainScreen.setText(mainText+"6");
        }
    }
    public void typeNumSeven(ActionEvent event) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            mainScreen.setText(mainText+"7");
        }
    }
    public void typeNumEight(ActionEvent event) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            mainScreen.setText(mainText+"8");
        }
    }
    public void typeNumNine(ActionEvent event) {
        String mainText = mainScreen.getText();
        if (mainText.length() < MAXLEN) {
            mainScreen.setText(mainText+"9");
        }
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
