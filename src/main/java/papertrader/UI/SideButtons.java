package papertrader.UI;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class SideButtons {

    private int buttonIterCount = 0;

    private final List<String> buttonList = List.of("Stocks", "Portfolio", "History");

    private void actionEvent(javafx.event.ActionEvent event) {
        System.out.println("Clicked button " +  event.toString());
        // TODO : Load the window here
    }



    private javafx.scene.control.Button buttonTemplate(String buttonText) {
        buttonIterCount++;
         javafx.scene.control.Button button = new javafx.scene.control.Button(buttonText);
         button.setOnAction(this::actionEvent);

         return button;
    }


    public GridPane loadButtons() {
        GridPane pane = new GridPane();
        for (String buttonText : buttonList) {
            pane.add(buttonTemplate(buttonText), 10, 25 * buttonIterCount);

        }

        return pane;
    }



}
