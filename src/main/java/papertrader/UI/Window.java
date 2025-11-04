package papertrader.UI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import papertrader.engine.MarketSystem;
import papertrader.player.Player;

import java.awt.*;
import java.util.*;

import java.util.List;
import java.util.function.Supplier;

public class Window extends Application {

    private final VBox root = new VBox();
    private final AnchorPane pane = new AnchorPane();

    private static final List<Map.Entry<String, Supplier<Pane>>> panelList = List.of(
            Map.entry("Stocks", Stocks::new),
            Map.entry("Portfolio", Portfolio::new),
            Map.entry("History", History::new)
    );

    @Override
    public void start(Stage stage) {

        Scene scene = new Scene(this.root, 800, 600);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm()
        );

        HBox sideButtons = new HBox(30);
        sideButtons.setAlignment(Pos.CENTER);
        for (Map.Entry<String, Supplier<Pane>> panel : panelList) {
            Button button = new Button(panel.getKey());
            button.getStyleClass().add("side_buttons");
            button.setOnAction(_ -> {
               setPane(panel.getValue().get());
            });
            sideButtons.getChildren().add(button);
        }

        this.root.getChildren().add(sideButtons);
        this.root.getChildren().add(this.pane);

        // Set Defaults
        setPane(panelList.getFirst().getValue().get());

        stage.setScene(scene);
        stage.setTitle("Paper Trader");
        stage.show();
    }

    @Override
    public void stop() {
        Player.get().saveData();
        MarketSystem.get().saveData();
    }

    private void setPane(Pane pane) {
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        this.pane.getChildren().setAll(pane);
    }

    public static void errorMessage(String error) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("");
        alert.setGraphic(null);
        alert.setTitle("Error");
        alert.setContentText(error);
        alert.showAndWait();
    }
}
