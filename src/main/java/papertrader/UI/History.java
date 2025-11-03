package papertrader.UI;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import papertrader.engine.MarketSystem;

import javax.swing.plaf.ScrollPaneUI;
import java.awt.*;

public class History extends VBox {

    private ScrollPane scrollPane = MakeScrollPane();

    History() {
        super(100);
        HBox hbox = new HBox();

        ScrollPane scrollPane = this.scrollPane;
        hbox.getChildren().add(this.scrollPane);

        this.getChildren().add(hbox);
    }

    public Label MakeHistoryButton(String Info) {
        Label label = new Label(Info);
        label.setPrefHeight(50);
        label.setPrefWidth(250);
        // css stuff here
        return label;
    }

    public ScrollPane MakeScrollPane() {
        ScrollPane pane = new ScrollPane();
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        pane.setFitToWidth(true);


        VBox vBox = new VBox();

        // placeholder to get it on the screen
        MarketSystem.get().stockList.forEach((string, stock) -> {

            Separator sep = new Separator();
            sep.setPrefHeight(25);
            vBox.getChildren().add(sep);

            Label label = MakeHistoryButton(string);

            vBox.getChildren().add(label);
        });

        pane.setContent(vBox);
        pane.setPannable(true);

        return pane;
    }

    public void PushHistory() {

    }

}