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

public class Stocks implements UIState {

    public VBox stockTemplate = buildStockTemplate();
    private ScrollPane scrollPane;
    private int scrollTotal = 0;


    @Override
    public Pane render() {
        System.out.println("Building Stocks");
        VBox pane = new VBox();
        TextField field = new TextField();
        field.setPromptText("Enter stock Ticker");
        field.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                System.out.println(field.getText());
            }
        });
        pane.getChildren().add(field);
        pane.getChildren().add(buildScrollPane());
        return pane;
    }

    private ScrollPane buildScrollPane() {
        if (this.scrollPane == null) {
            this.scrollPane = new ScrollPane();
            this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            this.scrollPane.fitToHeightProperty().set(true);
            this.scrollPane.setMinHeight(500);
            this.scrollPane.setOnScroll(event -> {
                scrollTotal++;
                //System.out.println(scrollTotal);
            });

            VBox vBox = new VBox();

            MarketSystem.get().stockList.forEach((string, stock) -> {

                Button button = new Button(string);
                //button.setOnAction(event -> actionEvent(event, buttonText));
                button.setFont(Window.MEDIUM_TEXT);
                button.setPrefWidth(200);

                vBox.getChildren().add(button);
            });

            this.scrollPane.setContent(vBox);
            this.scrollPane.setFitToWidth(true);
            this.scrollPane.setPannable(true);
        }
        return this.scrollPane;
    }


    private VBox buildStockTemplate() {
        VBox pane = new VBox();





        return pane;
    }
}
