package papertrader.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import papertrader.core.MarketSystem;
import papertrader.core.Player;

public class History extends VBox {

    private final VBox tradeList = new VBox(5);

    History() {
        super(10);
        this.setPadding(new Insets(20));

        Label titleLabel = new Label("Trade History");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        ScrollPane scrollPane = makeScrollPane();
        scrollPane.setPrefHeight(500);

        this.getChildren().addAll(titleLabel, scrollPane);

        refreshHistory();
    }

    public ScrollPane makeScrollPane() {
        ScrollPane pane = new ScrollPane();
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        pane.setFitToWidth(true);

        tradeList.setPadding(new Insets(10));

        pane.setContent(tradeList);
        pane.setPannable(true);

        return pane;
    }

    public void refreshHistory() {
        tradeList.getChildren().clear();

        var trades = Player.get().portfolio.getTrades();

        if (trades.isEmpty()) {
            Label emptyLabel = new Label("No trades yet. Start trading to see your history!");
            emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
            tradeList.getChildren().add(emptyLabel);
            return;
        }

        for (int i = trades.size() - 1; i >= 0; i--) {
            MarketSystem.Trade trade = trades.get(i);
            tradeList.getChildren().add(createTradeCard(trade, i + 1));
        }
    }

    private VBox createTradeCard(MarketSystem.Trade trade, int tradeNumber) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #444; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setMaxWidth(Region.USE_COMPUTED_SIZE);

        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label numberLabel = new Label("#" + tradeNumber);
        numberLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

        Label typeLabel = new Label(trade.type.toString());
        String typeColor = switch (trade.type) {
            case BUY -> "-fx-background-color: #28a745; -fx-text-fill: white;";
            case SELL -> "-fx-background-color: #dc3545; -fx-text-fill: white;";
            case SHORT -> "-fx-background-color: #ffc107; -fx-text-fill: black;";
            case COVER -> "-fx-background-color: #17a2b8; -fx-text-fill: white;";
        };
        typeLabel.setStyle(typeColor + " -fx-padding: 3 8 3 8; -fx-border-radius: 3; -fx-background-radius: 3; -fx-font-size: 11px; -fx-font-weight: bold;");

        headerBox.getChildren().addAll(numberLabel, typeLabel);

        Label stockLabel = new Label(trade.name);
        stockLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label sharesLabel = new Label(String.format("Shares: %.2f", trade.shares));
        Label priceLabel = new Label(String.format("Price: $%.2f", trade.shareValue));
        Label totalLabel = new Label(String.format("Total: $%.2f", trade.shares * trade.shareValue));
        totalLabel.setStyle("-fx-font-weight: bold;");

        sharesLabel.setStyle("-fx-font-size: 13px;");
        priceLabel.setStyle("-fx-font-size: 13px;");
        totalLabel.setStyle("-fx-font-size: 14px;");

        card.getChildren().addAll(headerBox, stockLabel, sharesLabel, priceLabel, totalLabel);

        return card;
    }

    public void pushHistory() {
        refreshHistory();
    }
}