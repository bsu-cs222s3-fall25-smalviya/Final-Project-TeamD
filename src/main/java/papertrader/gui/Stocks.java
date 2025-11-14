package papertrader.gui;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.util.StringConverter;
import papertrader.core.MarketSystem;
import papertrader.core.Player;
import papertrader.core.Time;

import java.util.function.*;

public class Stocks extends Window.SubPane {

    private final BorderPane contentPane = new BorderPane();
    private String currentStock;
    private Consumer<ActionEvent> currentMenu;
    private final VBox scrollBox = new VBox();

    Stocks() {
        TextField field = new TextField();
        field.setPromptText("Enter stock Ticker");

        field.textProperty().addListener((observable, oldValue, newValue) -> {
            refreshScrollBox(newValue);
        });

        this.setTop(field);

        BorderPane pane = new BorderPane();

        pane.setLeft(buildScrollPane());

        HBox buttonBox = new HBox(25.0);
        buttonBox.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        buttonBox.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        buttonBox.setAlignment(Pos.TOP_CENTER);

        BiFunction<String, Consumer<ActionEvent>, Button> buttonTemplate = (str, func) -> {
            Button button = new Button(str);
            button.setPrefWidth(100.0);
            button.setOnAction((event) -> {
                this.currentMenu = func;
                refresh(event);
            });
            return button;
        };

        this.contentPane.setTop(buttonBox);

        buttonBox.getChildren().add(buttonTemplate.apply("Trading", this::tradingMenu));
        //buttonBox.getChildren().add(buttonTemplate.apply("Hello2", (_) -> this.contentPane.setCenter(null)));
        buttonBox.getChildren().add(buttonTemplate.apply("Visualize", this::valueOverTimeChart));

        this.currentStock = MarketSystem.get().stockList.firstKey();
        this.currentMenu = this::tradingMenu;
        refresh(null);

        pane.setCenter(this.contentPane);

        this.setCenter(pane);
    }

    @Override
    public void refresh(ActionEvent event) {
        this.currentMenu.accept(event);
    }

    private ScrollPane buildScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(150.0);

        refreshScrollBox("");

        scrollPane.setContent(this.scrollBox);
        scrollPane.setPannable(true);

        return scrollPane;
    }

    private void refreshScrollBox(String filter) {
        this.scrollBox.getChildren().clear();

        MarketSystem.get().stockList.forEach((string, _) -> {
            if (string.startsWith(filter)) {
                Button button = new Button(string);
                button.setOnAction(_ -> onSelectStock(string));
                button.getStyleClass().add("button_list");

                this.scrollBox.getChildren().add(button);
            }
        });

    }


    private void tradingMenu(ActionEvent event) {
        if (this.currentStock.isEmpty()) return;

        BorderPane pane = new BorderPane();

        VBox infoBox = new VBox(10.0);
        infoBox.setPadding(new Insets(20.0));
        infoBox.setAlignment(Pos.CENTER);

        MarketSystem.Stock stock = MarketSystem.get().stockList.get(this.currentStock);

        javafx.scene.control.Label stockLabel = new javafx.scene.control.Label("Stock: " + this.currentStock);
        javafx.scene.control.Label priceLabel = new javafx.scene.control.Label(String.format("Current Price: $%.2f", stock.shareValue));
        javafx.scene.control.Label ownedLabel = new javafx.scene.control.Label(String.format("Shares Owned: %.2f", Player.get().portfolio.getNumberOfShares(this.currentStock)));
        javafx.scene.control.Label shortedLabel = new javafx.scene.control.Label(String.format("Shares Shorted: %.2f", Player.get().portfolio.getShortedShares(this.currentStock)));

        // Show short position P&L if exists
        String shortPnLText = "";
        if (Player.get().portfolio.hasShortPosition(this.currentStock)) {
            Player.Portfolio.ShortPosition pos = Player.get().portfolio.shortedStocks.get(this.currentStock);
            double currentValue = pos.shares * stock.shareValue;
            double initialValue = pos.shares * pos.initialPrice;
            double pnl = initialValue - currentValue; // Profit if price went down
            String pnlColor = pnl >= 0 ? "green" : "red";
            shortPnLText = String.format("Short P&L: $%.2f (Entry: $%.2f)", pnl, pos.initialPrice);
        }

        javafx.scene.control.Label shortPnLLabel = new javafx.scene.control.Label(shortPnLText);
        javafx.scene.control.Label moneyLabel = new javafx.scene.control.Label(String.format("Cash Available: $%.2f", Player.get().portfolio.getMoney()));
        javafx.scene.control.Label totalLabel = new javafx.scene.control.Label(String.format("Total Net Worth: $%.2f", Player.get().portfolio.getTotalMoney()));

        stockLabel.getStyleClass().add("bold");
        priceLabel.getStyleClass().add("small");
        ownedLabel.getStyleClass().add("small");
        shortedLabel.getStyleClass().add("small");
        shortPnLLabel.getStyleClass().addAll("small", "bold");
        moneyLabel.getStyleClass().add("small");
        totalLabel.getStyleClass().addAll("medium", "bold");

        if (shortPnLText.isEmpty()) {
            infoBox.getChildren().addAll(stockLabel, priceLabel, ownedLabel, shortedLabel, moneyLabel, totalLabel);
        } else {
            infoBox.getChildren().addAll(stockLabel, priceLabel, ownedLabel, shortedLabel, shortPnLLabel, moneyLabel, totalLabel);
        }

        pane.setCenter(infoBox);

        TextField sharesField = new TextField();
        sharesField.setPromptText("Enter number of shares");
        sharesField.setMaxWidth(200.0);

        VBox inputBox = new VBox(10.0);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(0.0, 0.0, 20.0, 0.0));
        inputBox.getChildren().add(sharesField);

        HBox box = new HBox(20.0);
        box.setPadding(new Insets(0.0, 0.0, 50.0, 0.0));
        box.setAlignment(Pos.BOTTOM_CENTER);

        Button buyButton = new Button("Buy");
        buyButton.setPrefWidth(100.0);
        buyButton.setOnAction(_ -> {
            try {
                double shares = Double.parseDouble(sharesField.getText());
                if (shares <= 0) {
                    Window.errorMessage("Please enter a positive number of shares!");
                    return;
                }
                Player.get().portfolio.buyStock(this.currentStock, shares);
                sharesField.clear();
                refresh(null);
            } catch (NumberFormatException e) {
                Window.errorMessage("Please enter a valid number!");
            }
        });

        Button sellButton = new Button("Sell");
        sellButton.setPrefWidth(100.0);
        sellButton.setOnAction(_ -> {
            try {
                double shares = Double.parseDouble(sharesField.getText());
                if (shares <= 0) {
                    Window.errorMessage("Please enter a positive number of shares!");
                    return;
                }
                if (!Player.get().portfolio.ownsStock(this.currentStock)) {
                    Window.errorMessage("You don't own any shares of " + this.currentStock + "!");
                    return;
                }
                Player.get().portfolio.sellStock(this.currentStock, shares);
                sharesField.clear();
                refresh(null);
            } catch (NumberFormatException e) {
                Window.errorMessage("Please enter a valid number!");
            }
        });

        Button shortButton = new Button("Short");
        shortButton.setPrefWidth(100.0);
        shortButton.setOnAction(_ -> {
            try {
                double shares = Double.parseDouble(sharesField.getText());
                if (shares <= 0) {
                    Window.errorMessage("Please enter a positive number of shares!");
                    return;
                }
                Player.get().portfolio.shortStock(this.currentStock, shares);
                sharesField.clear();
                refresh(null);
            } catch (NumberFormatException e) {
                Window.errorMessage("Please enter a valid number!");
            }
        });

        Button coverButton = new Button("Cover Short");
        coverButton.setPrefWidth(100.0);
        coverButton.setOnAction(_ -> {
            try {
                double shares = Double.parseDouble(sharesField.getText());
                if (shares <= 0) {
                    Window.errorMessage("Please enter a positive number of shares!");
                    return;
                }
                if (!Player.get().portfolio.hasShortPosition(this.currentStock)) {
                    Window.errorMessage("You don't have a short position in " + this.currentStock + "!");
                    return;
                }
                Player.get().portfolio.coverShort(this.currentStock, shares);
                sharesField.clear();
                refresh(null);
            } catch (NumberFormatException e) {
                Window.errorMessage("Please enter a valid number!");
            }
        });

        box.getChildren().addAll(buyButton, sellButton, shortButton, coverButton);

        VBox bottomBox = new VBox(10.0);
        bottomBox.getChildren().addAll(inputBox, box);

        pane.setBottom(bottomBox);

        this.contentPane.setCenter(pane);
    }

    private void valueOverTimeChart(ActionEvent event) {
        if (this.currentStock.isEmpty()) return;

        if (!MarketSystem.get().stockHistory.containsKey(this.currentStock)) {
            return;
        }


        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Days Since Jan 1st, 2024");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Share Value");

        final AreaChart<Number,Number> chart = new AreaChart<>(xAxis,yAxis);
        chart.setLegendVisible(false);
        chart.setPadding(new Insets(0.0, 60.0, 0.0, 0.0));

        //defining a series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(this.currentStock);

        StringConverter<Number> formatter = new StringConverter<>() {
            @Override
            public String toString(Number number) {
                int month = (int) Math.round(((number.doubleValue() % 365.0) / (365.0/12.0)) + 1.0);
                return Time.monthToString(month);
            }

            @Override
            public Number fromString(String s) {
                return Time.STRING_TO_MONTH.get(s);
            }
        };

        xAxis.setTickLabelFormatter(formatter);

        xAxis.setLowerBound(0.0);
        xAxis.setUpperBound(MarketSystem.get().stockHistory.get(this.currentStock).getFirst().getDaysSince((short) 2024) + 1);
        xAxis.setTickUnit(365.0/12.0);
        xAxis.setAutoRanging(false);

        for (MarketSystem.StockDate stockDate : MarketSystem.get().stockHistory.get(this.currentStock)) {
            series.getData().add(new XYChart.Data<>(stockDate.getDaysSince((short) 2024), stockDate.shareValue));
        }

        chart.getData().add(series);

        this.contentPane.setCenter(chart);
    }

    private void onSelectStock(String string) {
        if (!MarketSystem.get().stockList.containsKey(string)) {
            Window.errorMessage("Stock does not exist!");
            return;
        }
        this.currentStock = string;
        refresh(null);
    }
}
