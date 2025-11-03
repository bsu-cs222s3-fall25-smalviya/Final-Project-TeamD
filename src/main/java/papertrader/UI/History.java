package papertrader.UI;

import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import papertrader.engine.MarketSystem;

public class History extends VBox {

    History() {
        super(100);
        HBox hbox = new HBox();

        hbox.getChildren().add(makeScrollPane());

        this.getChildren().add(hbox);
    }

    public ScrollPane makeScrollPane() {
        ScrollPane pane = new ScrollPane();
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        pane.setFitToWidth(true);


        VBox vBox = new VBox();

        // placeholder to get it on the screen
        MarketSystem.get().stockList.forEach((string, stock) -> {
            Button button = new Button(string);
            button.getStyleClass().add("button_list");
            vBox.getChildren().add(button);
        });

        pane.setContent(vBox);
        pane.setPannable(true);

        return pane;
    }

    public void PushHistory() {

    }

}