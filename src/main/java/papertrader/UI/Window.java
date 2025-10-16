package papertrader.UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Window extends Application {

    private final BorderPane root = new BorderPane();
    private final StateMachine stateMachine = new StateMachine();

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(root, 800, 600);

        // Listen for state changes
        stateMachine.addListener(this::onStateChanged);

        // Add persistent sidebar
        SideButtons buttons = new SideButtons(stateMachine);
        VBox buttonBox = buttons.loadButtons();
        root.setLeft(buttonBox); // ✅ always visible on left

        // Load default state
        onStateChanged(stateMachine.GetState());

        stage.setScene(scene);
        stage.setTitle("Stock Market Game");
        stage.show();
    }

    private void onStateChanged(String newState) {
        root.setCenter(null);

        switch (newState) {
            case "Stocks":
                root.setCenter(new Stocks().buildStocks());
                break;
            case "Portfolio":
                GridPane portfolioPane = new GridPane();
                root.setCenter(portfolioPane);
                break;
            case "History":
                GridPane historyPane = new GridPane();
                root.setCenter(historyPane);
                break;
            default:
                root.setCenter(new GridPane());
                break;
        }
    }
}
