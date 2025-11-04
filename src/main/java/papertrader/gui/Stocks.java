package papertrader.gui;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import papertrader.core.MarketSystem;

public class Stocks extends VBox {

    private SwitchPane stockInfo = new SwitchPane();

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
        MarketSystem.Stock stock = MarketSystem.get().stockList.get(string);

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");

        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);

        lineChart.setTitle("Value vs. Time");
        //defining a series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(string);

        series.getData().add(new XYChart.Data<>(1.0, 23.0));
        series.getData().add(new XYChart.Data<>(2.0, 14.0));
        series.getData().add(new XYChart.Data<>(3.0, 15.0));

        lineChart.getData().add(series);

        stockInfo.set(lineChart);
    }
}
