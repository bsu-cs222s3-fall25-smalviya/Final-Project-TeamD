package papertrader.UI;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SideButtons {

    private final List<String> buttonList = List.of("Stocks", "Portfolio", "History");
    private final StateMachine stateMachine;

    private final Map<String, Supplier<UIState>> stateMap = Map.of(
            "Stocks", Stocks::new,
            "Portfolio", Portfolio::new,
            "History", History::new
    );

    public SideButtons(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    private void actionEvent(ActionEvent event, String buttonText) {
        UIState newState = stateMap.getOrDefault(buttonText, Stocks::new).get();
        stateMachine.changeState(newState);
    }

    private Button buttonTemplate(String buttonText) {
        Button button = new Button(buttonText);
        button.setOnAction(event -> actionEvent(event, buttonText));
        button.setPrefWidth(100);
        return button;
    }

    public VBox loadButtons() {
        VBox box = new VBox(30);
        box.setMaxWidth(100);
        box.setMinWidth(100);
        box.setMinHeight(850);
        box.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));

        for (String buttonText : buttonList) {
            box.getChildren().add(buttonTemplate(buttonText));
        }

        return box;
    }
}
