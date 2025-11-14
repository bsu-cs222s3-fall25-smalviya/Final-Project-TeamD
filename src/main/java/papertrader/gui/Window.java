package papertrader.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import papertrader.core.MarketSystem;
import papertrader.core.Player;

import java.util.*;

import java.util.List;

public class Window extends Application {

    private final BorderPane root = new BorderPane();

    private final List<Map.Entry<String, SubPane>> panelList = List.of(
            Map.entry("Stocks", new Stocks()),
            Map.entry("Portfolio", new Portfolio()),
            Map.entry("History", new History())
    );

    public static void main(String[] args) {
        MarketSystem.get().loadDefaultData();
        Player.get().loadDefaultData();

        Application.launch(Window.class, args);
    }

    @Override
    public void start(Stage stage) {

        Scene scene = new Scene(this.root, 800, 600);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm()
        );

        BorderPane topBar = new BorderPane();
        topBar.setPadding(new Insets(10));

        HBox sideButtons = new HBox(30);
        sideButtons.setAlignment(Pos.CENTER);
        for (Map.Entry<String, SubPane> panel : panelList) {
            Button button = new Button(panel.getKey());
            button.getStyleClass().add("side_buttons");
            button.setOnAction(event -> {
                this.root.setCenter(panel.getValue());
                panel.getValue().refresh(event);
            });
            sideButtons.getChildren().add(button);
        }

        Button simulateButton = new Button("Simulate");
        simulateButton.getStyleClass().add("side_buttons");
        simulateButton.setOnAction(event -> {
            MarketSystem.get().incrementStocks();
            if (this.root.getCenter() instanceof SubPane pane) {
                pane.refresh(event);
            }
        });

        topBar.setCenter(sideButtons);
        topBar.setRight(simulateButton);

        this.root.setTop(topBar);

        this.root.setCenter(panelList.getFirst().getValue());

        stage.setScene(scene);
        stage.setTitle("Paper Trader");
        stage.show();
    }

    @Override
    public void stop() {
        Player.get().saveData();
        MarketSystem.get().saveData();
    }

    public static void errorMessage(String error) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("");
        alert.setGraphic(null);
        alert.setTitle("Error");
        alert.setContentText(error);
        alert.showAndWait();
    }

    public abstract static class SubPane extends BorderPane {
        public abstract void refresh(ActionEvent event);
    }
}