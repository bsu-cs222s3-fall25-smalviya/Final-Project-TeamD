package papertrader.UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import papertrader.engine.MarketSystem;
import papertrader.player.Player;

public class Window extends Application {

    public static final Font LARGE_TEXT = Font.font(25);
    public static final Font MEDIUM_TEXT = Font.font(18);
    public static final Font SMALL_TEXT = Font.font(14);

    private final VBox root = new VBox();
    private final StateMachine stateMachine = new StateMachine();

    @Override
    public void start(Stage stage) {

        Scene scene = new Scene(root, 800, 600);

        stateMachine.addListener(this::onStateChanged);

        SideButtons buttons = new SideButtons(stateMachine);
        root.getChildren().add(buttons.loadButtons());

        onStateChanged(stateMachine.getState());

        stage.setScene(scene);
        stage.setTitle("Stock Market Game");
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
