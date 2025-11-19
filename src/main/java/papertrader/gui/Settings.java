package papertrader.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Settings extends BorderPane implements IRefreshable {

    private final WindowGUI window;
    private final Label themeLabel = new Label();

    public Settings(WindowGUI window) {
        this.window = window;
        refresh(null);

        VBox settingsBox = new VBox();

        Slider themeSlider = new Slider();
        themeSlider.setBlockIncrement(1.0);
        themeSlider.setSnapToTicks(true);
        themeSlider.setMajorTickUnit(1.0);
        themeSlider.setMinorTickCount(0);
        themeSlider.setShowTickLabels(true);
        themeSlider.setShowTickMarks(true);
        themeSlider.setValue(0.0);
        themeSlider.setMin(0.0);
        themeSlider.setMax(2.0);

        themeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int value = Math.round(newValue.floatValue());
            themeSlider.setValue(value);
            this.window.setTheme(WindowGUI.Theme.values()[value]);
        });

        settingsBox.getChildren().addAll(themeSlider, this.themeLabel);

        this.setLeft(settingsBox);
    }

    @Override
    public void refresh(ActionEvent event) {
        this.themeLabel.setText("Theme: " + this.window.getTheme());
    }
}
