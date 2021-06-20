package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public void startWindow(Stage primaryStage, String fxml, String title) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        Scene scene = new Scene(root, 500, 800);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }
    @Override
    public void start(Stage primaryStage) throws Exception {
        startWindow(primaryStage, "/fxml/main.fxml", "EqnoCal");
    }
    public void startScientificMode() throws  Exception {
        "/fxml/scientific.fxml"
    }
}
