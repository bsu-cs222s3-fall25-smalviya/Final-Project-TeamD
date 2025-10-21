package papertrader.UI;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

import java.awt.*;

public class Portfolio implements UIState {

    private String filterSetting = "Start";
    private final GridPane grid = new GridPane();
    private final VBox vbox = new VBox(10);

    private List<String> filterButtons = List.of("Start of Game", "This Year", "Past Month", "Last Week");
    @Override
    public Pane render() {
        this.grid.getChildren().add(vbox);
        MakeButtons();
        return this.grid;
    }

    private void MakeButtons() {
        for (String buttonName : filterButtons) {
            Button button = new Button(buttonName);
            button.setFont(Window.SMALL_TEXT);
            this.vbox.getChildren().add(button);
        }
    }
}