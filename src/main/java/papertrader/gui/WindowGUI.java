package papertrader.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import papertrader.core.MarketSystem;
import papertrader.core.Player;
import papertrader.core.Time;

import java.util.*;

import java.util.List;

public class WindowGUI extends Application implements IRefreshable {

    private final BorderPane root = new BorderPane();
    private Scene scene;
    private Theme theme;

    private final Stocks stockMenu = new Stocks(this);
    private final History historyMenu = new History(this);
    private final Display display = new Display(this);
    private final Simulate simulateMenu = new Simulate(this);
    private final Settings settings = new Settings(this);

    private final List<Map.Entry<String, EventHandler<ActionEvent>>> panelList = List.of(
            Map.entry("Stocks", (event) -> setPanel(this.stockMenu, event)),
            Map.entry("History", (event) -> setPanel(this.historyMenu, event)),
            Map.entry("Simulate", (event) -> {
                this.simulateMenu.increment();
                setPanel(this.simulateMenu, event);
            }),
            Map.entry("Settings", (event) -> setPanel(this.settings, event))
    );

    public static void main(String[] args) {
        MarketSystem.get().loadData();
        Player.get().loadData();

        Application.launch(WindowGUI.class, args);
    }

    @Override
    public void start(Stage stage) {

        this.scene = new Scene(this.root, 800, 600);

        HBox sideButtons = new HBox(30);
        sideButtons.setPadding(new Insets(5.0, 25.0, 5.0, 25.0));
        sideButtons.setAlignment(Pos.CENTER);

        for (Map.Entry<String, EventHandler<ActionEvent>> panel : panelList) {
            Button button = new Button(panel.getKey());
            button.getStyleClass().add("side_buttons");
            button.setOnAction(panel.getValue());
            sideButtons.getChildren().add(button);
        }

        VBox topDisplay = new VBox();
        topDisplay.getChildren().add(sideButtons);

        topDisplay.getChildren().add(display);

        this.root.setTop(topDisplay);

        panelList.getFirst().getValue().handle(null);
        this.setTheme(Player.get().portfolio.currentTheme);

        stage.setScene(this.scene);
        stage.setTitle("Paper Trader");
        stage.show();
    }

    public void refresh(ActionEvent event) {
        display.refresh(event);
        ((IRefreshable)this.root.getCenter()).refresh(event);
    }

    public void setPanel(IRefreshable panel, ActionEvent event) {
        this.root.setCenter((Node)panel);
        refresh(event);
    }

    public Theme getTheme() {
        return this.theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;

        this.scene.getStylesheets().clear();

        String themePath = "/style_" + theme.name().toLowerCase() + ".css";

        this.scene.getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource(themePath)).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm()
        );

        refresh(null);
    }

    @Override
    public void stop() {
        Player.get().saveData();
        MarketSystem.get().saveData();
    }

    public static void errorMessage(String error) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("");
        alert.setGraphic(null);
        alert.setTitle("Error");
        alert.setContentText(error);
        alert.showAndWait();
    }

    public enum Theme {
        DARK("Dark"),
        LIGHT("Light"),
        CREAM("Cream");

        private final String name;

        Theme(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}