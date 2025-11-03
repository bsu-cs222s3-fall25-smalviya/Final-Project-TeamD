package papertrader.UI;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import papertrader.engine.MarketSystem;
import papertrader.player.Player;
import javafx.scene.paint.Color;

import java.awt.*;

import java.util.Objects;

public class Window extends Application {

    private final VBox root = new VBox();
    private final StateMachine stateMachine = new StateMachine();

    @Override
    public void start(Stage stage) {

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm()
        );

        stateMachine.addListener(this::onStateChanged);

        SideButtons buttons = new SideButtons(stateMachine);
        root.getChildren().add(buttons.loadButtons());

        onStateChanged(stateMachine.getState());

        stage.setScene(scene);
        stage.setTitle("Paper Trader");
        stage.show();
    }

    @Override
    public void stop() {
        Player.get().saveData();
        MarketSystem.get().saveData();
    }

    private void onStateChanged(Pane newState) {
        if (root.getChildren().size() < 2) {
            root.getChildren().add(newState);
            return;
        }
        root.getChildren().set(1, newState);
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
