package papertrader.UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import papertrader.engine.MarketSystem;
import papertrader.player.Player;

public class Window extends Application {

    private final BorderPane root = new BorderPane();
    private final StateMachine stateMachine = new StateMachine();

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(root, 800, 600);

        stateMachine.addListener(this::onStateChanged);

        SideButtons buttons = new SideButtons(stateMachine);
        root.setLeft(buttons.loadButtons());

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

    private void onStateChanged(UIState newState) {
        root.setCenter(newState.render());
    }
}
