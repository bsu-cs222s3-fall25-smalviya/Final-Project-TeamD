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
import javafx.scene.layout.HBox;
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
        double previousValue = Player.get().portfolio.getTotalMoney();

        MarketSystem.get().incrementStocks();

        double nextValue = Player.get().portfolio.getTotalMoney();

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

        HBox buttonBox = new HBox(15.0);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10.0, 0.0, 10.0, 0.0));

        Button sellAllButton = new Button("Sell All Stocks");
        sellAllButton.getStyleClass().add("action-button");
        sellAllButton.setOnAction(e -> {
            sellAllStocks();
            this.window.refresh(e);
        });

        Button coverAllButton = new Button("Cover All Shorts");
        coverAllButton.getStyleClass().add("action-button");
        coverAllButton.setOnAction(e -> {
            coverAllShorts();
            this.window.refresh(e);
        });

        buttonBox.getChildren().addAll(sellAllButton, coverAllButton);

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
                double sharesOwned = Player.get().portfolio.getNumberOfShares(stock.getKey());
                double sharesShorted = Player.get().portfolio.getShortedShares(stock.getKey());

                if (sharesOwned > 0 || sharesShorted > 0) {
                    TableStock tableStock = new TableStock();
                    tableStock.tickerProperty().set(stock.getKey());
                    tableStock.shareValueProperty().set(String.format("$%.2f", stock.getValue().shareValue));

                    double changeValue = stock.getValue().shareValue - MarketSystem.get().stockHistory.get(stock.getKey()).getFirst().shareValue;
                    if (changeValue > 0.0) {
                        tableStock.changeProperty().set(String.format("$%.2f ↑", changeValue));
                    } else {
                        tableStock.changeProperty().set(String.format("$%.2f ↓", changeValue));
                    }

                    tableStock.ownedProperty().set(String.format("%.2f", sharesOwned));
                    tableStock.shortedProperty().set(String.format("%.2f", sharesShorted));
                    stocks.add(tableStock);
                }
            }

            tableView.setItems(stocks);
        }

        vbox.getChildren().addAll(label, moneyMade, networthlabel, buttonBox, tableView);

        this.setCenter(vbox);
    }

    private void sellAllStocks() {
        List<String> stocksToSell = new ArrayList<>();

        for (var entry : MarketSystem.get().stockList.entrySet()) {
            double sharesOwned = Player.get().portfolio.getNumberOfShares(entry.getKey());
            if (sharesOwned > 0) {
                stocksToSell.add(entry.getKey());
            }
        }

        for (String stockName : stocksToSell) {
            double sharesOwned = Player.get().portfolio.getNumberOfShares(stockName);

            if (sharesOwned > 0) {
                MarketSystem.Stock stock = MarketSystem.get().stockList.get(stockName);
                double transactionAmount = sharesOwned * stock.shareValue;
                double fee = MarketSystem.get().calculateTradingFee(transactionAmount);

                Player.get().portfolio.sellStock(stockName, sharesOwned);

                Player.get().portfolio.removeMoney((int)fee);
            }
        }
    }

    private void coverAllShorts() {
        List<String> shortsToCover = new ArrayList<>();

        for (var entry : MarketSystem.get().stockList.entrySet()) {
            double shortedShares = Player.get().portfolio.getShortedShares(entry.getKey());
            if (shortedShares > 0) {
                shortsToCover.add(entry.getKey());
            }
        }

        for (String stockName : shortsToCover) {
            double shortedShares = Player.get().portfolio.getShortedShares(stockName);
            MarketSystem.Stock stock = MarketSystem.get().stockList.get(stockName);

            if (stock != null && shortedShares > 0) {
                double transactionAmount = shortedShares * stock.shareValue;
                double fee = MarketSystem.get().calculateTradingFee(transactionAmount);

                if (Player.get().portfolio.getMoney() >= fee) {
                    Player.get().portfolio.coverShort(stockName, shortedShares);
                    Player.get().portfolio.removeMoney((int)fee);
                } else {
                    System.out.println("Insufficient funds to cover short position in " + stockName);
                }
            }
        }
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