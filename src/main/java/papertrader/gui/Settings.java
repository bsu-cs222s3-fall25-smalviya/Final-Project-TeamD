package papertrader.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import papertrader.core.MarketSystem;
import papertrader.core.Player;

public class Settings extends BorderPane implements IRefreshable {
    private WindowGUI.Theme currentTheme = WindowGUI.Theme.DARK;
    private final Instructions instructionMenu;
    private final WindowGUI window;

    private double marketVolatility = 1.0;
    private int simulationSpeed = 1;

    public Settings(WindowGUI window) {
        this.window = window;
        this.instructionMenu = new Instructions(window);
        refresh(null);

        VBox settingsBox = new VBox(20.0);
        settingsBox.setAlignment(Pos.TOP_CENTER);
        settingsBox.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));

        // Theme settings
        Button themeButton = new Button("Change Theme: Dark");
        themeButton.setOnAction(event -> {
            int next = (currentTheme.ordinal() + 1) % WindowGUI.Theme.values().length;
            currentTheme = WindowGUI.Theme.values()[next];
            this.window.setTheme(currentTheme);
            themeButton.setText("Change Theme: " + currentTheme.getName());
        });

        Label volatilityLabel = new Label("Market Volatility: Normal");
        Slider volatilitySlider = new Slider(0.5, 2.0, 1.0);
        volatilitySlider.setShowTickLabels(true);
        volatilitySlider.setShowTickMarks(true);
        volatilitySlider.setMajorTickUnit(0.5);
        volatilitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            marketVolatility = newVal.doubleValue();
            String level = marketVolatility < 0.8 ? "Low" :
                    marketVolatility > 1.3 ? "High" : "Normal";
            volatilityLabel.setText("Market Volatility: " + level);
            MarketSystem.get().setVolatility(marketVolatility);
        });
        VBox volatilityBox = new VBox(5, volatilityLabel, volatilitySlider);
        volatilityBox.setAlignment(Pos.CENTER);

        Label speedLabel = new Label("Simulation Speed: 1x");
        Slider speedSlider = new Slider(1, 5, 1);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1);
        speedSlider.setSnapToTicks(true);
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            simulationSpeed = newVal.intValue();
            speedLabel.setText("Simulation Speed: " + simulationSpeed + "x");
            MarketSystem.get().setSimulationSpeed(simulationSpeed);
        });
        VBox speedBox = new VBox(5, speedLabel, speedSlider);
        speedBox.setAlignment(Pos.CENTER);


        CheckBox tradingFeesCheckBox = new CheckBox("Enable Trading Fees");
        tradingFeesCheckBox.setSelected(true);
        tradingFeesCheckBox.setOnAction(event -> {
            boolean feesEnabled = tradingFeesCheckBox.isSelected();
            MarketSystem.get().setTradingFeesEnabled(feesEnabled);
        });

        CheckBox realTimeCheckBox = new CheckBox("Use Real Market Hours");
        realTimeCheckBox.setSelected(false);
        realTimeCheckBox.setOnAction(event -> {
            boolean realTime = realTimeCheckBox.isSelected();
            MarketSystem.get().setRealTimeMode(realTime);
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
                volatilityBox,
                speedBox,
                tradingFeesCheckBox,
                realTimeCheckBox,
                newGame,
                instructions
        );

        this.setCenter(settingsBox);
    }

    @Override
    public void refresh(ActionEvent event) {}
}