package papertrader.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import papertrader.core.MarketSystem;
import papertrader.core.Player;

import java.util.*;

import java.util.List;
import java.util.function.Supplier;

public class Window extends Application {

    private final VBox root = new VBox();
    private final SwitchPane pane = new SwitchPane();

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

        HBox sideButtons = new HBox(30);
        sideButtons.setAlignment(Pos.CENTER);
        for (Map.Entry<String, Supplier<Pane>> panel : panelList) {
            Button button = new Button(panel.getKey());
            button.getStyleClass().add("side_buttons");
            button.setOnAction(_ -> {
               this.pane.set(panel.getValue().get());
            });
            sideButtons.getChildren().add(button);
        }

        this.root.getChildren().add(sideButtons);
        this.root.getChildren().add(this.pane);

        // Set Defaults
        this.pane.set(panelList.getFirst().getValue().get());

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
