package papertrader.UI;
import com.sun.javafx.scene.control.InputField;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

public class Stocks implements UIState {

    public VBox stockTemplate = buildStockTemplate();
    private ScrollPane scrollPane =  buildScrollPane();
    private int scrollTotal = 0;


    @Override
    public Pane render() {
        System.out.println("Building Stocks");
        VBox pane = new VBox();
        TextField field = new TextField();
        field.setPromptText("Enter stock Ticker");
        field.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                System.out.println(field.getText());
            }
        });
        pane.getChildren().add(field);
        pane.getChildren().add(scrollPane);
        return pane;
    }

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

        this.scrollPane.setFitToWidth(true);
        this.scrollPane.setPannable(true);

        return  this.scrollPane;
    }


    private VBox buildStockTemplate() {
        VBox pane = new VBox();





        return pane;
    }
}
