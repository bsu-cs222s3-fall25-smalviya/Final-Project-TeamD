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

public class Window extends Application implements IRefreshable {

    private final BorderPane root = new BorderPane();

    private final Stocks stockMenu = new Stocks(this);
    private final Portfolio portfolioMenu = new Portfolio(this);
    private final History historyMenu = new History(this);
    private final Display display = new Display(this);

    private final List<Map.Entry<String, EventHandler<ActionEvent>>> panelList = List.of(
            Map.entry("Stocks", (event) -> setPanel(this.stockMenu, event)),
            Map.entry("Portfolio", (event) -> setPanel(this.portfolioMenu, event)),
            Map.entry("History", (event) -> setPanel(this.historyMenu, event)),
            Map.entry("Simulate", (event) -> {
                MarketSystem.get().incrementStocks();
                Time.incrementDate();
                refresh(event);
            })
    );

    public static void main(String[] args) {
        MarketSystem.get().loadDefaultData();
        Player.get().loadDefaultData();

        Application.launch(Window.class, args);
    }

    @Override
    public void start(Stage stage) {

        Scene scene = new Scene(this.root, 800, 600);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm()
        );

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
        refresh(null);

        stage.setScene(scene);
        stage.setTitle("Paper Trader");
        stage.show();
    }

    public void refresh(ActionEvent event) {
        display.refresh(event);
        ((IRefreshable)this.root.getCenter()).refresh(event);
    }

    private void setPanel(IRefreshable panel, ActionEvent event) {
        this.root.setCenter((Node)panel);
        refresh(event);
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
}