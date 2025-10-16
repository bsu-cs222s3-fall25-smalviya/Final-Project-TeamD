package papertrader.UI;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;


public class SideButtons {

    private final List<String> buttonList = List.of("Stocks", "Portfolio", "History");

    private void actionEvent(ActionEvent event) {
        System.out.println("Clicked button " + event.toString());
    }

    private Button buttonTemplate(String buttonText) {
        Button button = new Button(buttonText);
        button.setOnAction(this::actionEvent);
        button.setPrefWidth(100);
        return button;
    }

    public VBox loadButtons() {
        VBox box = new VBox(30);
        box.setMaxWidth(100);
        box.setMinHeight(850);
        Background fill = new Background(new BackgroundFill(Color.GREEN, null, null));
        box.setBackground(fill);
        for (String buttonText : buttonList) {
            box.getChildren().add(buttonTemplate(buttonText));
        }
        return box;
    }
}



