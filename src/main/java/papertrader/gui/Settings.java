package papertrader.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import papertrader.core.MarketSystem;
import papertrader.core.Player;

public class Settings extends BorderPane implements IRefreshable {
    private final Instructions instructionMenu;
    private final WindowGUI window;

    public Settings(WindowGUI window) {
        this.window = window;
        this.instructionMenu = new Instructions(window);
        refresh(null);

        VBox settingsBox = new VBox(20.0);
        settingsBox.setAlignment(Pos.TOP_CENTER);
        settingsBox.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));

        // Theme settings
        Button themeButton = new Button("Change Theme: " + Player.get().portfolio.currentTheme.getName());
        themeButton.setOnAction(event -> {
            int next = (Player.get().portfolio.currentTheme.ordinal() + 1) % WindowGUI.Theme.values().length;
            Player.get().portfolio.currentTheme = WindowGUI.Theme.values()[next];
            this.window.setTheme(Player.get().portfolio.currentTheme);
            themeButton.setText("Change Theme: " + Player.get().portfolio.currentTheme.getName());
        });

        CheckBox tradingFeesCheckBox = new CheckBox("Enable Trading Fees");
        tradingFeesCheckBox.setSelected(Player.get().portfolio.useTradingFees);
        tradingFeesCheckBox.setOnAction(event -> {
            Player.get().portfolio.useTradingFees = tradingFeesCheckBox.isSelected();
        });

        Button newGame = new Button("Start New Game");
        newGame.setOnAction(event -> {
            MarketSystem.get().loadDefaultData();
            Player.get().loadDefaultData();
            this.window.refresh(event);
        });

        Button instructions = new Button("View Instructions");
        instructions.setOnAction(event -> window.setPanel(this.instructionMenu, event));

        settingsBox.getChildren().addAll(
                themeButton,
                tradingFeesCheckBox,
                newGame,
                instructions
        );

        this.setCenter(settingsBox);
    }

    @Override
    public void refresh(ActionEvent event) {}
}