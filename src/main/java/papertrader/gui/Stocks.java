package papertrader.gui;
import javafx.event.ActionEvent;
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
import papertrader.core.Time;

import java.util.function.*;

public class Stocks extends VBox {

    private final BorderPane contentPane = new BorderPane();
    private String currentStock;
    private Consumer<ActionEvent> currentMenu;

    Stocks() {
        TextField field = new TextField();
        field.setPromptText("Enter stock Ticker");
        field.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onSelectStock(field.getText());
            }
        });
        this.getChildren().add(field);

        BorderPane pane = new BorderPane();

        ScrollPane scrollPane = buildScrollPane();
        pane.setLeft(scrollPane);

        HBox buttonBox = new HBox(25.0);
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

        buttonBox.getChildren().add(buttonTemplate.apply("Visualize", this::valueOverTimeChart));
        buttonBox.getChildren().add(buttonTemplate.apply("Hello1", (_) -> {this.contentPane.setCenter(null);}));
        buttonBox.getChildren().add(buttonTemplate.apply("Hello2", (_) -> {this.contentPane.setCenter(null);}));

        this.currentStock = MarketSystem.get().stockList.firstKey();
        this.currentMenu = this::valueOverTimeChart;
        refresh(null);

        pane.setCenter(this.contentPane);

        this.getChildren().add(pane);
    }

    private void refresh(ActionEvent event) {
        currentMenu.accept(event);
    }

    private ScrollPane buildScrollPane() {
        ScrollPane pane = new ScrollPane();
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        pane.setFitToWidth(true);

        VBox vBox = new VBox();

        MarketSystem.get().stockList.forEach((string, _) -> {
            Button button = new Button(string);
            button.setOnAction(_ -> onSelectStock(string));
            button.getStyleClass().add("button_list");

            vBox.getChildren().add(button);
        });

        pane.setContent(vBox);
        pane.setPannable(true);

        return pane;
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
