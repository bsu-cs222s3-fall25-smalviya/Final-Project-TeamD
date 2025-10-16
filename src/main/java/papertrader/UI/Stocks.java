package papertrader.UI;
import com.sun.javafx.scene.control.InputField;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

public class Stocks {

    private ScrollPane scrollPane =  buildScrollPane();
    private int scrollTotal = 0;

    private ScrollPane buildScrollPane() {
        this.scrollPane = new ScrollPane();
        this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.scrollPane.fitToHeightProperty();
        this.scrollPane.setMinHeight(5000);
        this.scrollPane.setOnScroll(event -> {
            scrollTotal++;
            System.out.println(scrollTotal);
        });

        return  this.scrollPane;
    }

    public VBox buildStocks() {
        System.out.println("Building Stocks");
        VBox pane = new VBox();
        TextField field = new TextField();
        field.setPromptText("Enter stock Ticker");
        pane.getChildren().add(field);
        pane.getChildren().add(scrollPane);
        return pane;
    }
}
