package papertrader.gui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import papertrader.core.MarketSystem;
import papertrader.core.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javafx.scene.control.TableColumn.DEFAULT_CELL_FACTORY;

public class Simulate extends BorderPane implements IRefreshable {

    private final WindowGUI window;
    private double moneyChanged = 0.0;

    public Simulate(WindowGUI window) {
        this.window = window;
    }

    public void increment() {
        // Get value of all stocks before incrementing
        double previousValue = Player.get().portfolio.getTotalMoney();

        MarketSystem.get().incrementStocks();

        // Get value of all stocks after incrementing
        double nextValue = Player.get().portfolio.getTotalMoney();

        // Calculate how money changed and give a message depending on it.
        this.moneyChanged = nextValue - previousValue;

        refresh(null);
    }

    @Override
    public void refresh(ActionEvent event) {

        VBox vbox = new VBox(10.0);
        vbox.setPadding(new Insets(10.0, 0.0, 10.0, 0.0));
        vbox.setAlignment(Pos.TOP_CENTER);

        Label label = new Label("Results: ");
        label.getStyleClass().addAll("bold");

        KeyValueLabel moneyMade = new KeyValueLabel("$%.2f!");

        if (moneyChanged > 0.0) {
            moneyMade.setKey("You made ");
            moneyMade.setValueColor(Color.GREEN);
            moneyMade.setValue(moneyChanged);
        } else {
            moneyMade.setKey("You lost ");
            moneyMade.setValueColor(Color.RED);
            moneyMade.setValue(-moneyChanged);
        }

        KeyValueLabel networthlabel = new KeyValueLabel("Your net worth is now ", "$%.2f!");
        networthlabel.setValueColor(Color.GREEN);
        networthlabel.setValue(Player.get().portfolio.getTotalMoney());

        TableView<TableStock> tableView = new TableView<>();

        {

            TableColumn<TableStock, String> ticker = new TableColumn<>("Name");
            TableColumn<TableStock, String> value = new TableColumn<>("Share Value");
            TableColumn<TableStock, String> change = new TableColumn<>("Change");
            TableColumn<TableStock, String> amount = new TableColumn<>("Shares Owned");
            TableColumn<TableStock, String> shorted = new TableColumn<>("Shares Shorted");

            ticker.setCellValueFactory(new PropertyValueFactory<>("ticker"));
            value.setCellValueFactory(new PropertyValueFactory<>("shareValue"));
            change.setCellValueFactory(new PropertyValueFactory<>("change"));
            amount.setCellValueFactory(new PropertyValueFactory<>("owned"));
            shorted.setCellValueFactory(new PropertyValueFactory<>("shorted"));

            value.setCellFactory(_ -> new TableCell<>() {
                protected void updateItem(String var1, boolean var2) {
                    if (!Objects.equals(var1, this.getItem())) {
                        super.updateItem(var1, var2);
                        if (var1 == null) {
                            super.setText(null);
                            super.setGraphic(null);
                            return;
                        }
                        super.setText(var1);
                        super.setGraphic(null);
                        super.setTextFill(Color.GREEN);
                    }
                }
            });

            change.setCellFactory(_ -> new TableCell<>() {
                protected void updateItem(String var1, boolean var2) {
                    if (!Objects.equals(var1, this.getItem())) {
                        super.updateItem(var1, var2);
                        if (var1 == null) {
                            super.setText(null);
                            super.setGraphic(null);
                            return;
                        }
                        super.setText(var1);
                        super.setGraphic(null);
                        if (var1.contains("↑")) {
                            super.setTextFill(Color.GREEN);
                        } else {
                            super.setTextFill(Color.RED);
                        }

                    }
                }
            });

            amount.setCellFactory(_ -> new TableCell<>() {
                protected void updateItem(String var1, boolean var2) {
                    if (!Objects.equals(var1, this.getItem())) {
                        super.updateItem(var1, var2);
                        if (var1 == null) {
                            super.setText(null);
                            super.setGraphic(null);
                            return;
                        }
                        super.setText(var1);
                        super.setGraphic(null);
                        if (Double.parseDouble(var1) > 0.0) {
                            super.setTextFill(Color.GREEN);
                        } else {
                            super.setTextFill(Color.RED);
                        }

                    }
                }
            });

            shorted.setCellFactory(_ -> new TableCell<>() {
                protected void updateItem(String var1, boolean var2) {
                    if (!Objects.equals(var1, this.getItem())) {
                        super.updateItem(var1, var2);
                        if (var1 == null) {
                            super.setText(null);
                            super.setGraphic(null);
                            return;
                        }
                        super.setText(var1);
                        super.setGraphic(null);
                        if (Double.parseDouble(var1) > 0.0) {
                            super.setTextFill(Color.GREEN);
                        } else {
                            super.setTextFill(Color.RED);
                        }

                    }
                }
            });

            tableView.getColumns().addAll(List.of(ticker, value, change, amount, shorted));

            ObservableList<TableStock> stocks = FXCollections.observableArrayList();

            for (var stock : MarketSystem.get().stockList.entrySet()) {
                if (Player.get().portfolio.ownsStock(stock.getKey())) {
                    TableStock tableStock = new TableStock();
                    tableStock.tickerProperty().set(stock.getKey());
                    tableStock.shareValueProperty().set(String.format("$%.2f", stock.getValue().shareValue));

                    double changeValue = stock.getValue().shareValue - MarketSystem.get().stockHistory.get(stock.getKey()).getFirst().shareValue;
                    if (changeValue > 0.0) {
                        tableStock.changeProperty().set(String.format("$%.2f ↑", changeValue));
                    } else {
                        tableStock.changeProperty().set(String.format("$%.2f ↓", changeValue));

                    }

                    tableStock.ownedProperty().set(String.format("%.2f", Player.get().portfolio.getNumberOfShares(stock.getKey())));
                    tableStock.shortedProperty().set(String.format("%.2f", Player.get().portfolio.getShortedShares(stock.getKey())));
                    stocks.add(tableStock);
                }
            }

            tableView.setItems(stocks);
        }

        vbox.getChildren().addAll(label, moneyMade, networthlabel, tableView);

        this.setCenter(vbox);
    }

    public static class TableStock {
        private final StringProperty ticker = new SimpleStringProperty("None");
        private final StringProperty shareValue = new SimpleStringProperty("0.0");
        private final StringProperty change = new SimpleStringProperty("0.0");
        private final StringProperty owned = new SimpleStringProperty("0.0");
        private final StringProperty shorted = new SimpleStringProperty("0.0");

        public StringProperty tickerProperty() { return ticker; }
        public StringProperty shareValueProperty() { return shareValue; }
        public StringProperty changeProperty() { return change; }
        public StringProperty ownedProperty() { return owned; }
        public StringProperty shortedProperty() { return shorted; }
    }
}
