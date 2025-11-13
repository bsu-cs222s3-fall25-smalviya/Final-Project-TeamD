package papertrader.gui;

import javafx.application.Application;
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
import java.util.function.Supplier;

public class Window extends Application {

    private final BorderPane root = new BorderPane();
    private Supplier<Pane> currentPanelSupplier;

    private static final List<Map.Entry<String, Supplier<Pane>>> panelList = List.of(
            Map.entry("Stocks", Stocks::new),
            Map.entry("Portfolio", Portfolio::new),
            Map.entry("History", History::new)
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
        for (Map.Entry<String, Supplier<Pane>> panel : panelList) {
            Button button = new Button(panel.getKey());
            button.getStyleClass().add("side_buttons");
            button.setOnAction(_ -> {
                currentPanelSupplier = panel.getValue();
                this.root.setCenter(panel.getValue().get());
            });
            sideButtons.getChildren().add(button);
        }

        Button simulateButton = new Button("Simulate");
        simulateButton.getStyleClass().add("side_buttons");
        simulateButton.setOnAction(_ -> {
            MarketSystem.get().incrementStocks();
            if (currentPanelSupplier != null) {
                this.root.setCenter(currentPanelSupplier.get());
            }
        });

        topBar.setCenter(sideButtons);
        topBar.setRight(simulateButton);

        this.root.setTop(topBar);

        currentPanelSupplier = panelList.getFirst().getValue();
        this.root.setCenter(currentPanelSupplier.get());

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
}