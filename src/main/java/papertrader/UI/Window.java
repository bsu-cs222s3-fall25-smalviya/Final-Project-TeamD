package papertrader.UI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Window extends Application {

    @Override
    public void start(Stage stage) throws Exception {


        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 10;");

        Scene scene = new Scene(root, 600, 400);
        SideButtons buttons = new SideButtons();
        VBox buttonBox = buttons.loadButtons();
        root.getChildren().add(buttonBox);
        stage.setScene(scene);
        stage.setTitle("Stock Market Game");
        stage.show();
    }
}
