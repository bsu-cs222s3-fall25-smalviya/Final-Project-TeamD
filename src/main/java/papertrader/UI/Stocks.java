package papertrader.UI;
import com.sun.javafx.scene.control.InputField;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import papertrader.engine.MarketSystem;

import java.util.Map;

import static papertrader.UI.Window.MEDIUM_TEXT;

public class Stocks extends VBox {

    Stocks() {
        TextField field = new TextField();
        field.setPromptText("Enter stock Ticker");
        field.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onSelectStock(field.getText());
            }
        });
        this.getChildren().add(field);
        HBox hbox = new HBox();

        ScrollPane scrollPane = buildScrollPane();
        hbox.getChildren().add(scrollPane);

        this.getChildren().add(hbox);
    }

    private ScrollPane buildScrollPane() {
        ScrollPane pane = new ScrollPane();
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        pane.setFitToWidth(true);

        VBox vBox = new VBox();

        MarketSystem.get().stockList.forEach((string, stock) -> {

            Button button = new Button(string);
            button.setOnAction(event -> onSelectStock(string));
            button.setFont(Window.MEDIUM_TEXT);
            button.setPrefWidth(150);

            vBox.getChildren().add(button);
        });

        pane.setContent(vBox);
        pane.setPannable(true);

        return pane;
    }

    private void onSelectStock(String stock) {
        System.out.println(stock);
        if (!MarketSystem.get().stockList.containsKey(stock)) {
            Window.errorMessage("Stock does not exist!");
            return;
        }

    }
}
