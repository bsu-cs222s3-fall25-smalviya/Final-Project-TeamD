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

public class Stocks {


    public VBox buildStocks() {
        System.out.println("Building Stocks");
        VBox pane = new VBox();
        TextField field = new TextField();
        field.setPromptText("Enter stock Ticker");
        pane.getChildren().add(field);
        return pane;
    }
}
