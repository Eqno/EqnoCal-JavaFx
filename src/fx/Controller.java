package fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML public Label mainScreen;
    public void AC(ActionEvent event) {
        mainScreen.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
