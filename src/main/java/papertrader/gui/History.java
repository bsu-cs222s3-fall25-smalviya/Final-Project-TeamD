package papertrader.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import papertrader.core.MarketSystem;
import papertrader.core.Player;

public class History extends Window.SubPane {

    private final VBox tradeList = new VBox(5);

    public History() {
        VBox vbox = new VBox(10);
        this.setPadding(new Insets(20));
        this.getStyleClass().add("root");

        Label titleLabel = new Label("Trade History");
        titleLabel.getStyleClass().add("history_title");

        ScrollPane scrollPane = makeScrollPane();
        scrollPane.setPrefHeight(500);

        vbox.getChildren().addAll(titleLabel, scrollPane);
        this.setCenter(vbox);

        refresh(null);
    }

    @Override
    public void refresh(ActionEvent event) {
        tradeList.getChildren().clear();

        var trades = Player.get().portfolio.getTrades();

        if (trades.isEmpty()) {
            Label emptyLabel = new Label("You have no trade history.");
            emptyLabel.getStyleClass().add("history_empty");
            tradeList.getChildren().add(emptyLabel);
            return;
        }

        for (int i = trades.size() - 1; i >= 0; i--) {
            MarketSystem.Trade trade = trades.get(i);
            tradeList.getChildren().add(createTradeCard(trade, i + 1));
        }
    }

    public ScrollPane makeScrollPane() {
        ScrollPane pane = new ScrollPane();
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        pane.setFitToWidth(true);
        pane.setPannable(true);

        tradeList.setPadding(new Insets(10));
        pane.setContent(tradeList);

        return pane;
    }

    private VBox createTradeCard(MarketSystem.Trade trade, int tradeNumber) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.getStyleClass().add("trade_card");
        card.setMaxWidth(Region.USE_COMPUTED_SIZE);

        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label numberLabel = new Label("#" + tradeNumber);
        numberLabel.getStyleClass().add("trade_number");

        Label typeLabel = new Label(trade.type.toString());
        switch (trade.type) {
            case BUY -> typeLabel.getStyleClass().add("trade_type_buy");
            case SELL -> typeLabel.getStyleClass().add("trade_type_sell");
            case SHORT -> typeLabel.getStyleClass().add("trade_type_short");
            case COVER -> typeLabel.getStyleClass().add("trade_type_cover");
        }

        headerBox.getChildren().addAll(numberLabel, typeLabel);

        Label stockLabel = new Label(trade.name);
        stockLabel.getStyleClass().add("trade_stock_name");

        Label sharesLabel = new Label(String.format("Shares: %.2f", trade.shares));
        sharesLabel.getStyleClass().add("trade_detail");

        Label priceLabel = new Label(String.format("Price: $%.2f", trade.shareValue));
        priceLabel.getStyleClass().add("trade_detail");

        Label totalLabel = new Label(String.format("Total: $%.2f", trade.shares * trade.shareValue));
        totalLabel.getStyleClass().add("trade_total");

        card.getChildren().addAll(headerBox, stockLabel, sharesLabel, priceLabel, totalLabel);

        return card;
    }
}
