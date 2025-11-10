package papertrader.gui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.util.StringConverter;
import papertrader.core.MarketSystem;
import papertrader.core.Time;

public class Stocks extends VBox {

    private final SwitchPane stockInfo = new SwitchPane();

    Stocks() {
        TextField field = new TextField();
        field.setPromptText("Enter stock Ticker");
        field.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onSelectStock(field.getText());
            }
        });
        this.getChildren().add(field);
        HBox hbox = new HBox();

        ScrollPane scrollPane = buildScrollPane();
        hbox.getChildren().add(scrollPane);
        hbox.getChildren().add(this.stockInfo);

        this.getChildren().add(hbox);
    }

    private ScrollPane buildScrollPane() {
        ScrollPane pane = new ScrollPane();
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        pane.setFitToWidth(true);

        VBox vBox = new VBox();

        MarketSystem.get().stockList.forEach((string, stock) -> {
            Button button = new Button(string);
            button.setOnAction(event -> onSelectStock(string));
            button.getStyleClass().add("button_list");

            vBox.getChildren().add(button);
        });

        pane.setContent(vBox);
        pane.setPannable(true);

        return pane;
    }

    private void onSelectStock(String string) {
        if (!MarketSystem.get().stockList.containsKey(string)) {
            Window.errorMessage("Stock does not exist!");
            return;
        }
        if (!MarketSystem.get().stockHistory.containsKey(string)) {
            return;
        }

        HBox hbox = new HBox();

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days Since Jan 1st, 2024");

        final AreaChart<Number,Number> chart = new AreaChart<>(xAxis,yAxis);
        chart.setTitle("Share Value over Time");

        //defining a series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(string);

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
        xAxis.setUpperBound(MarketSystem.get().stockHistory.get(string).getFirst().getDaysSince((short) 2024) + 1);
        xAxis.setTickUnit(365.0/12.0);
        xAxis.setAutoRanging(false);

        for (MarketSystem.StockDate stockDate : MarketSystem.get().stockHistory.get(string)) {
            series.getData().add(new XYChart.Data<>(stockDate.getDaysSince((short) 2024), stockDate.shareValue));
        }

        chart.getData().add(series);

        hbox.getChildren().add(chart);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_RIGHT);

        vbox.getChildren().add(new Button("Hello"));
        vbox.getChildren().add(new Button("Hello1"));
        vbox.getChildren().add(new Button("Hello2"));

        hbox.getChildren().add(vbox);

        stockInfo.set(hbox);
    }
}
