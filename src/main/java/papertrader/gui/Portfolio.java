package papertrader.gui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class Portfolio extends BorderPane implements IRefreshable {

    private final WindowGUI window;

    private String filterSetting = "Start";

    private final List<String> filterButtons = List.of("Start of Game", "This Year", "Past Month", "Last Week");

    public Portfolio(WindowGUI window) {
        this.window = window;

        VBox vbox = new VBox(10);

        MakeButtons(vbox);

        this.setCenter(vbox);
    }

    @Override
    public void refresh(ActionEvent event) {

    }

    private void MakeButtons(VBox vbox) {
        for (String buttonName : filterButtons) {
            Button button = new Button(buttonName);
            button.getStyleClass().add("stat");
            vbox.getChildren().add(button);
            button.setOnAction(event -> {
                System.out.println("Hello ");
                filterSetting = buttonName;
                System.out.println(filterSetting);
            });
        }
    }

}