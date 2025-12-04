package papertrader.gui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import papertrader.core.MarketSystem;
import papertrader.core.Player;

public class Settings extends BorderPane implements IRefreshable {

    private WindowGUI.Theme currentTheme = WindowGUI.Theme.DARK;
    private final WindowGUI window;

    public Settings(WindowGUI window) {
        this.window = window;
        refresh(null);

        VBox settingsBox = new VBox();

        Button themeButton = new Button("Change Theme: Dark");

        themeButton.setOnAction(event -> {
            int next = (currentTheme.ordinal() + 1) % WindowGUI.Theme.values().length;
            currentTheme = WindowGUI.Theme.values()[next];
            this.window.setTheme(currentTheme);
            themeButton.setText("Change Theme: " + currentTheme.getName());
        });

        Button newGame = new Button("Start New Game");

        newGame.setOnAction(event -> {
            MarketSystem.get().loadDefaultData();
            Player.get().loadDefaultData();
            this.window.refresh(event);
        });

        settingsBox.getChildren().addAll(themeButton, newGame);

        this.setLeft(settingsBox);
    }

    @Override
    public void refresh(ActionEvent event) {}
}
