package papertrader.UI;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
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
        button.setFont(Window.MEDIUM_TEXT);
        button.setOnAction(event -> actionEvent(event, buttonText));
        button.setMinWidth(200);
        return button;
    }

    public HBox loadButtons() {
        HBox box = new HBox(30);
        box.setFillHeight(true);
        box.alignmentProperty().set(Pos.CENTER);
        box.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));

        for (String buttonText : buttonList) {
            box.getChildren().add(buttonTemplate(buttonText));
        }

        return box;
    }
}
