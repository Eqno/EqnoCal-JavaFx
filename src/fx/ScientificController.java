package fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ScientificController extends KeyboardController implements Initializable {
    @FXML public Label mainScreen;
    @FXML public Label secondScreen;

    public void normalMode(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/normal.fxml")));
        mainScreen.getScene().setRoot(root);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setMainScreen(mainScreen);
        setSecondScreen(secondScreen);
    }
}
