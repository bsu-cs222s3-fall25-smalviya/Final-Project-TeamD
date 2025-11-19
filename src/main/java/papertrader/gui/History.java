package papertrader.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import papertrader.core.MarketSystem;
import papertrader.core.Player;

public class History extends BorderPane implements IRefreshable {

    private final WindowGUI window;

    private final VBox tradeList = new VBox(5);

    public History(WindowGUI window) {
        this.window = window;

        VBox vbox = new VBox(10);
        this.setPadding(new Insets(20));

        Label titleLabel = new Label("Trade History");
        titleLabel.getStyleClass().addAll("large", "bold");

        ScrollPane scrollPane = makeScrollPane();
        scrollPane.setPrefHeight(500);

        vbox.getChildren().addAll(titleLabel, scrollPane);
        this.setCenter(vbox);
    }

    @Override
    public void refresh(ActionEvent event) {
        tradeList.getChildren().clear();

        var trades = Player.get().portfolio.getTrades();

        if (trades.isEmpty()) {
            Label emptyLabel = new Label("You have no trade history.");
            emptyLabel.getStyleClass().addAll("small", "inactive");
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
        numberLabel.getStyleClass().addAll("tiny", "bold");

        Label typeLabel = new Label(trade.type.toString());
        typeLabel.getStyleClass().add("trade_type");
        switch (trade.type) {
            case BUY -> typeLabel.getStyleClass().add("buy");
            case SELL -> typeLabel.getStyleClass().add("sell");
            case SHORT -> typeLabel.getStyleClass().add("short");
            case COVER -> typeLabel.getStyleClass().add("cover");
        }

        headerBox.getChildren().addAll(numberLabel, typeLabel);

        Label stockLabel = new Label(trade.name);
        stockLabel.getStyleClass().addAll("medium", "bold");

        Label timeLabel = new Label("Time: " + trade.stockDate.date.toString());
        timeLabel.getStyleClass().addAll("medium", "bold");

        Label sharesLabel = new Label(String.format("Shares: %.2f", trade.stockDate.shares));
        sharesLabel.getStyleClass().add("small");

        Label priceLabel = new Label(String.format("Price: $%.2f", trade.stockDate.shareValue));
        priceLabel.getStyleClass().add("small");

        Label totalLabel = new Label(String.format("Total: $%.2f", trade.stockDate.shares * trade.stockDate.shareValue));
        totalLabel.getStyleClass().addAll("small", "bold");

        card.getChildren().addAll(headerBox, stockLabel, timeLabel, sharesLabel, priceLabel, totalLabel);

        return card;
    }
}
